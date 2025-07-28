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
import androidx.compose.ui.graphics.asSkiaBitmap
import cocoapods.TOCropViewController.*
import org.jetbrains.skia.Image

// Global callbacks
private var mainGalleryCallback: ((ImageBitmap?) -> Unit)? = null
private var mainCameraCallback: ((ImageBitmap?) -> Unit)? = null

@Composable
actual fun rememberImagePicker(): ImagePicker {
    return remember {
        IOSImagePicker()
    }
}

class IOSImagePicker : ImagePicker {

    // Delegate referansını class seviyesinde saklıyoruz, GC'ye gitmesin diye
    private var cropDelegate: CropViewControllerDelegate? = null

    @OptIn(ExperimentalForeignApi::class)
    private fun presentCropViewController(image: UIImage, aspectRatio: Float, finalCallback: (ImageBitmap?) -> Unit) {

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
                        completion = {
                        }
                    )
                } else {
                    finalCallback(null)
                }
            } catch (e: Exception) {
                finalCallback(null)
            }
        }
    }

    override fun pickFromGallery(onResult: (ImageBitmap?) -> Unit) {
        mainGalleryCallback = onResult

        val picker = UIImagePickerController()
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        picker.mediaTypes = listOf("public.image")
        picker.delegate = GalleryPickerDelegate(this)

        getCurrentViewController()?.presentViewController(picker, true, null)
    }

    override fun pickFromCamera(onResult: (ImageBitmap?) -> Unit) {
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
        onResult(true) // iOS otomatik permission handling
    }

    override fun cropImage(
        image: ImageBitmap,
        aspectRatio: Float,
        onCropped: (ImageBitmap?) -> Unit
    ) {

        val uiImage = image.toUIImage()
        if (uiImage == null) {
            onCropped(null)
            return
        }

        presentCropViewController(uiImage, aspectRatio, onCropped)
    }

    internal fun handleGalleryImage(image: UIImage) {
        val bitmap = image.toImageBitmap()
        mainGalleryCallback?.invoke(bitmap)
        mainGalleryCallback = null
    }

    internal fun handleCameraImage(image: UIImage) {
        val bitmap = image.toImageBitmap()
        mainCameraCallback?.invoke(bitmap)
        mainCameraCallback = null
    }

    internal fun handleGalleryCancel() {
       // mainGalleryCallback?.invoke(null)
        mainGalleryCallback = null
    }

    internal fun handleCameraCancel() {
       // mainCameraCallback?.invoke(null)
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
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        if (image == null) {
            picker.dismissViewControllerAnimated(true) {
                imagePicker.handleCameraCancel()
            }
            return
        }

        picker.dismissViewControllerAnimated(true) {
            imagePicker.handleCameraImage(image)
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
    private val onResult: (ImageBitmap?) -> Unit
) : NSObject(), TOCropViewControllerDelegateProtocol {

    override fun cropViewController(
        cropViewController: TOCropViewController,
        didCropToImage: UIImage,
        withRect: CValue<CGRect>,
        angle: Long
    ) {
        val bitmap = didCropToImage.toImageBitmap()
        cropViewController.dismissViewControllerAnimated(true) {
            onResult(bitmap)
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
