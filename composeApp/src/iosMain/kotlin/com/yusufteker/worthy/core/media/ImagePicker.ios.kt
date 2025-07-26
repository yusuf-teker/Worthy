package com.yusufteker.worthy.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.*
import platform.AVFoundation.*
import platform.Foundation.*
import platform.Photos.*
import platform.UIKit.*
import org.jetbrains.skia.Image


// iosMain/src/iosMain/kotlin/ImagePicker.ios.kt
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.*
import platform.AVFoundation.*
import platform.Foundation.*
import platform.Photos.*
import platform.UIKit.*
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
actual fun rememberImagePicker(): ImagePicker {
    return remember { IOSImagePicker() }
}

class IOSImagePicker : ImagePicker {
    private var currentCallback: ((ImageBitmap?) -> Unit)? = null
    private var currentDelegate: ImagePickerDelegate? = null

    override fun pickFromGallery(onResult: (ImageBitmap?) -> Unit) {
        currentCallback = onResult

        val picker = UIImagePickerController()
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        picker.allowsEditing = false

        currentDelegate = ImagePickerDelegate { image ->
            val bitmap = image?.let { convertUIImageToImageBitmap(it) }
            currentCallback?.invoke(bitmap)
            currentCallback = null
            currentDelegate = null
        }

        picker.delegate = currentDelegate
        presentViewController(picker)
    }

    override fun pickFromCamera(onResult: (ImageBitmap?) -> Unit) {
        currentCallback = onResult

        if (!isCameraAvailable()) {
            onResult(null)
            return
        }

        val picker = UIImagePickerController()
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        picker.allowsEditing = false

        currentDelegate = ImagePickerDelegate { image ->
            val bitmap = image?.let { convertUIImageToImageBitmap(it) }
            currentCallback?.invoke(bitmap)
            currentCallback = null
            currentDelegate = null
        }

        picker.delegate = currentDelegate
        presentViewController(picker)
    }

    override fun isCameraAvailable(): Boolean {
        return UIImagePickerController.isSourceTypeAvailable(
            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        )
    }

    override fun requestCameraPermission(onResult: (Boolean) -> Unit) {
        val permissionChecker = IOSPermissionChecker()
        permissionChecker.requestCameraPermission(onResult)
    }

    override fun cropImage(image: ImageBitmap, onCropped: (ImageBitmap?) -> Unit) {
        onCropped(image) // iOS'ta crop işlemi için özel bir implementasyon yok
    }

    private fun presentViewController(controller: UIViewController) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(controller, animated = true, completion = null)
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun convertUIImageToImageBitmap(uiImage: UIImage): ImageBitmap? {
        return try {
            val imageData = UIImagePNGRepresentation(uiImage) ?: return null

            val bytes = ByteArray(imageData.length.toInt())
            imageData.bytes?.let { dataBytes ->
                bytes.usePinned { pinnedBytes ->
                    memcpy(pinnedBytes.addressOf(0), dataBytes, imageData.length)
                }
            }

            val skiaImage = Image.makeFromEncoded(bytes)
            skiaImage.toComposeImageBitmap()
        } catch (e: Exception) {
            println("Error converting UIImage to ImageBitmap: ${e.message}")
            null
        }
    }
}
// Ayrı delegate sınıfı
class ImagePickerDelegate(
    private val onImageSelected: (UIImage?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        onImageSelected(image)
        picker.dismissViewControllerAnimated(true, completion = null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        onImageSelected(null)
        picker.dismissViewControllerAnimated(true, completion = null)
    }
}
