
package com.yusufteker.worthy.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.*
import platform.CoreGraphics.CGRect
import platform.darwin.NSObject
import platform.posix.memcpy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import cocoapods.TOCropViewController.*
import io.github.aakira.napier.Napier
import org.jetbrains.skia.Image
import platform.CoreGraphics.*

// Global callbacks
private var mainGalleryCallback: ((PlatformImage?) -> Unit)? = null
private var mainCameraCallback: ((PlatformImage?) -> Unit)? = null

@Composable
actual fun rememberImagePicker(): ImagePicker {
    return remember {
        IOSImagePicker()
    }
}


// NSData extension (eğer yoksa ekleyin)
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val bytes = ByteArray(this.length.toInt())
    memScoped {
        memcpy(bytes.refTo(0), this@toByteArray.bytes, this@toByteArray.length)
    }
    return bytes
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun loadImageBitmapFromPath(path: String): ImageBitmap? {
    return try {
        // Dosya adını çıkar
        val fileName = path.substringAfterLast("/")
        Napier.d("Loading image: $fileName")

        // Documents directory al ve yeni yolu oluştur
        val documentsDirectory = NSFileManager.defaultManager.URLsForDirectory(
            directory = NSDocumentDirectory,
            inDomains = NSUserDomainMask
        ).firstOrNull() as? NSURL ?: return null

        val fileURL = documentsDirectory.URLByAppendingPathComponent(fileName) ?: return null
        val currentPath = fileURL.path ?: return null

        // Dosya var mı kontrol et
        if (!NSFileManager.defaultManager.fileExistsAtPath(currentPath)) {
            Napier.d("File not found: $fileName")
            return null
        }

        // NSData oluştur
        val nsData = NSData.dataWithContentsOfFile(currentPath) ?: return null

        // ImageBitmap'e çevir
        val byteArray = nsData.toByteArray()
        val skiaImage = org.jetbrains.skia.Image.makeFromEncoded(byteArray) ?: return null
        val imageBitmap = skiaImage.toComposeImageBitmap()

        Napier.d("Image loaded successfully: ${imageBitmap.width}x${imageBitmap.height}")
        imageBitmap

    } catch (e: Exception) {
        Napier.e("Error loading image: ${e.message}")
        null
    }
}

class IOSImagePicker : ImagePicker {

    // Delegate referansını class seviyesinde saklıyoruz, GC'ye gitmesin diye
    private var cropDelegate: CropViewControllerDelegate? = null

    @OptIn(ExperimentalForeignApi::class)
    private fun presentCropViewController(image: UIImage, aspectRatio: Float, finalCallback: (PlatformImage?) -> Unit) {

        NSOperationQueue.mainQueue.addOperationWithBlock {
            try {
                val cropViewController = TOCropViewController(image)

                // Aspect ratio ayarı: TOCropViewController presetleri sınırlı, float oranına göre en yakın preset seçiyoruz
                cropViewController.aspectRatioLockEnabled = true
                cropViewController.aspectRatioPickerButtonHidden = true
                cropViewController.resetButtonHidden = true
                cropViewController.rotateButtonsHidden = false

                cropViewController.aspectRatioPreset = when {
                    aspectRatio == 1f -> TOCropViewControllerAspectRatioPreset.TOCropViewControllerAspectRatioPresetSquare
                    aspectRatio > 1.7f -> TOCropViewControllerAspectRatioPreset.TOCropViewControllerAspectRatioPreset16x9
                    aspectRatio > 1.3f -> TOCropViewControllerAspectRatioPreset.TOCropViewControllerAspectRatioPreset4x3
                    aspectRatio > 1.1f -> TOCropViewControllerAspectRatioPreset.TOCropViewControllerAspectRatioPreset3x2
                    else -> TOCropViewControllerAspectRatioPreset.TOCropViewControllerAspectRatioPresetOriginal
                }

                cropDelegate = CropViewControllerDelegate(finalCallback)
                cropViewController.delegate = cropDelegate

                val rootViewController = getCurrentViewController()
                if (rootViewController != null) {
                    rootViewController.presentViewController(
                        cropViewController,
                        animated = true,
                        completion = {}
                    )
                } else {
                    finalCallback(null)
                }
            } catch (e: Exception) {
                finalCallback(null)
            }
        }
    }

    override fun pickFromGallery(onResult: (PlatformImage?) -> Unit) {
        mainGalleryCallback = onResult

        val picker = UIImagePickerController()
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        picker.mediaTypes = listOf("public.image")
        picker.delegate = GalleryPickerDelegate(this)

        getCurrentViewController()?.presentViewController(picker, true, null)
    }

    override fun pickFromCamera(onResult: (PlatformImage?) -> Unit) {
        mainCameraCallback = onResult

        if (!isCameraAvailable()) {
            onResult(null)
            return
        }
        launchCamera()
    }

    private fun launchCamera() {
        val picker = UIImagePickerController()
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        picker.mediaTypes = listOf("public.image")
        picker.delegate = CameraPickerDelegate(this)

        getCurrentViewController()?.presentViewController(picker, true, null)
    }

    override fun isCameraAvailable(): Boolean {
        return UIImagePickerController.isSourceTypeAvailable(
            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        )
    }

    override fun requestCameraPermission(onResult: (Boolean) -> Unit) {
        onResult(true)
    }

    override fun cropImage(
        image: PlatformImage,
        aspectRatio: Float,
        onCropped: (PlatformImage?) -> Unit
    ) {
        // Basit çözüm: Sadece TOCropViewController kullan
        val uiImage = image.uiImage
        if (uiImage == null) {
            onCropped(null)
            return
        }
        presentCropViewController(uiImage, aspectRatio, onCropped)
    }

    internal fun handleGalleryImage(image: UIImage) {
        // Optimized conversion kullan
        //val bitmap = image.toImageBitmap()
        mainGalleryCallback?.invoke(PlatformImage(image))
        mainGalleryCallback = null
    }

    internal fun handleCameraImage(image: UIImage) {
        //val bitmap = image.toImageBitmap()
        mainCameraCallback?.invoke(PlatformImage(image))
        mainCameraCallback = null
    }

    internal fun handleGalleryCancel() {
        mainGalleryCallback = null
    }

    internal fun handleCameraCancel() {
        mainCameraCallback = null
    }

    private fun getCurrentViewController(): UIViewController? {
        val keyWindow = UIApplication.sharedApplication.keyWindow
        val rootViewController = keyWindow?.rootViewController

        var topViewController = rootViewController
        while (topViewController?.presentedViewController != null) {
            topViewController = topViewController.presentedViewController
        }

        return topViewController
    }
}

// Delegates
class GalleryPickerDelegate(
    private val imagePicker: IOSImagePicker
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        if (image == null) {
            picker.dismissViewControllerAnimated(true) {
                imagePicker.handleGalleryCancel()
            }
            return
        }

        picker.dismissViewControllerAnimated(true) {
            imagePicker.handleGalleryImage(image)
        }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true) {
            imagePicker.handleGalleryCancel()
        }
    }
}

class CameraPickerDelegate(
    private val imagePicker: IOSImagePicker
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        NSOperationQueue.mainQueue.addOperationWithBlock {
            val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
            if (image == null) {
                picker.dismissViewControllerAnimated(true) {
                    imagePicker.handleCameraCancel()
                }
            } else {
                picker.dismissViewControllerAnimated(true) {
                    imagePicker.handleCameraImage(image)
                }
            }
        }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true) {
            imagePicker.handleCameraCancel()
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
class CropViewControllerDelegate(
    private val onResult: (PlatformImage?) -> Unit
) : NSObject(), TOCropViewControllerDelegateProtocol {

    override fun cropViewController(
        cropViewController: TOCropViewController,
        didCropToImage: UIImage,
        withRect: CValue<CGRect>,
        angle: Long
    ) {
        // Optimized conversion kullan
        //val bitmap = didCropToImage.toImageBitmapOptimized() ?: didCropToImage.toImageBitmap()
        cropViewController.dismissViewControllerAnimated(true) {
            onResult(PlatformImage(didCropToImage))
        }
    }

    override fun cropViewController(
        cropViewController: TOCropViewController,
        didFinishCancelled: Boolean
    ) {
        cropViewController.dismissViewControllerAnimated(true) {
            onResult(null)
        }
    }
}

// Extension functions for conversion
@OptIn(ExperimentalForeignApi::class)
fun UIImage.toImageBitmap(): ImageBitmap? {
    return try {
        val imageData = UIImagePNGRepresentation(this) ?: return null
        val bytes = ByteArray(imageData.length.toInt())
        memcpy(bytes.refTo(0), imageData.bytes, imageData.length)

        val skiaImage = Image.makeFromEncoded(bytes)
        skiaImage.toComposeImageBitmap()
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage? {
    return try {
        val skiaImage = Image.makeFromBitmap(this.asSkiaBitmap())
        val encodedData = skiaImage.encodeToData(org.jetbrains.skia.EncodedImageFormat.PNG)
            ?: return null

        val byteArray = encodedData.bytes

        val nsData = byteArray.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = byteArray.size.toULong())
        }

        UIImage.imageWithData(nsData)
    } catch (e: Exception) {
        null
    }
}

actual class PlatformImage(val uiImage: UIImage)

actual fun PlatformImage.toImageBitmap(): ImageBitmap {
    Napier.d("Converting UIImage to ImageBitmap")
    // UIKit UIImage -> Compose ImageBitmap dönüşümü için
    return uiImage.toImageBitmap()!! // Burada iOS için extension yazman gerekebilir
}
