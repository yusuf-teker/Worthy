package com.yusufteker.worthy.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.*

import cocoapods.TOCropViewController.TOCropViewController
import cocoapods.TOCropViewController.TOCropViewControllerAspectRatioPreset
import platform.CoreGraphics.CGRect
import platform.darwin.NSObject
import platform.posix.memcpy

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import cocoapods.TOCropViewController.*
import io.github.aakira.napier.Napier
import org.jetbrains.skia.Image

// Global callbacks
private var galleryCallback: ((ImageBitmap?) -> Unit)? = null
private var cameraCallback: ((ImageBitmap?) -> Unit)? = null
private var cropCallback: ((ImageBitmap?) -> Unit)? = null
private var permissionCallback: ((Boolean) -> Unit)? = null

@Composable
actual fun rememberImagePicker(): ImagePicker {
    return remember {
        IOSImagePicker()
    }
}

class IOSImagePicker : ImagePicker {

    override fun pickFromGallery(onResult: (ImageBitmap?) -> Unit) {
        galleryCallback = onResult

        val picker = UIImagePickerController()
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        picker.mediaTypes = listOf("public.image")
        picker.delegate = GalleryPickerDelegate()

        val rootViewController = getCurrentViewController()
        rootViewController?.presentViewController(picker, true, null)
    }

    override fun pickFromCamera(onResult: (ImageBitmap?) -> Unit) {
        cameraCallback = onResult

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
        picker.delegate = CameraPickerDelegate()

        val rootViewController = getCurrentViewController()
        rootViewController?.presentViewController(picker, true, null)
    }

    override fun isCameraAvailable(): Boolean {
        return UIImagePickerController.isSourceTypeAvailable(
            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        )
    }

    override fun requestCameraPermission(onResult: (Boolean) -> Unit) {
        onResult(true) // iOS'ta otomatik permission request
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun cropImage(
        image: ImageBitmap,
        aspectRatio: Float,
        onCropped: (ImageBitmap?) -> Unit
    ) {
        Napier.d("IOSImagePicker.cropImage called with aspectRatio: $aspectRatio")
        cropCallback = onCropped

        val uiImage = image.toUIImage()
        if (uiImage == null) {
            onCropped(null)
            return
        }

        val cropViewController = TOCropViewController(uiImage)

        // 1:1 aspect ratio settings
        cropViewController.aspectRatioPreset =
            TOCropViewControllerAspectRatioPreset.TOCropViewControllerAspectRatioPresetSquare
        cropViewController.aspectRatioLockEnabled = true
        cropViewController.aspectRatioPickerButtonHidden = true
        cropViewController.resetButtonHidden = true
        cropViewController.rotateButtonsHidden = false

        cropViewController.delegate = CropViewControllerDelegate()

        val rootViewController = getCurrentViewController()
        rootViewController?.presentViewController(cropViewController, true, null)
    }

    private fun getCurrentViewController(): UIViewController? {
        return UIApplication.sharedApplication.keyWindow?.rootViewController
    }
}

// Delegates
class GalleryPickerDelegate : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        val bitmap = image?.toImageBitmap()

        galleryCallback?.invoke(bitmap)
        galleryCallback = null

        picker.dismissViewControllerAnimated(true, null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        galleryCallback?.invoke(null)
        galleryCallback = null

        picker.dismissViewControllerAnimated(true, null)
    }
}

class CameraPickerDelegate : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        val bitmap = image?.toImageBitmap()

        cameraCallback?.invoke(bitmap)
        cameraCallback = null

        picker.dismissViewControllerAnimated(true, null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        cameraCallback?.invoke(null)
        cameraCallback = null

        picker.dismissViewControllerAnimated(true, null)
    }
}

@OptIn(ExperimentalForeignApi::class)
class CropViewControllerDelegate : NSObject(), TOCropViewControllerDelegateProtocol {

    override fun cropViewController(
        cropViewController: TOCropViewController,
        didCropToImage: UIImage,
        withRect: CValue<CGRect>,
        angle: Long
    ) {
        val bitmap = didCropToImage.toImageBitmap()

        cropCallback?.invoke(bitmap)
        cropCallback = null

        cropViewController.dismissViewControllerAnimated(true, null)
    }

    override fun cropViewController(
        cropViewController: TOCropViewController,
        didFinishCancelled: Boolean
    ) {
        cropCallback?.invoke(null)
        cropCallback = null

        cropViewController.dismissViewControllerAnimated(true, null)
    }
}

// Extension Functions - Bu fonksiyonları kendiniz yazıyorsunuz
@OptIn(ExperimentalForeignApi::class)
fun UIImage.toImageBitmap(): ImageBitmap? {
    return try {
        val imageData = UIImagePNGRepresentation(this) ?: return null
        val bytes = ByteArray(imageData.length.toInt())
        memcpy(bytes.refTo(0), imageData.bytes, imageData.length)

        val skiaImage = Image.makeFromEncoded(bytes)
        skiaImage.toComposeImageBitmap()
    } catch (e: Exception) {
        println("Error converting UIImage to ImageBitmap: ${e.message}")
        null
    }
}

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage? {
    return try {
        // ImageBitmap'i Skia Image'a çevir
        val skiaImage = org.jetbrains.skia.Image.makeFromBitmap(this.asSkiaBitmap())

        // PNG formatında encode et
        val encodedData = skiaImage.encodeToData(org.jetbrains.skia.EncodedImageFormat.PNG)
            ?: return null

        val byteArray = encodedData.bytes

        // ByteArray'i NSData'ya çevir
        val nsData = byteArray.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = byteArray.size.toULong())
        }

        // NSData'dan UIImage oluştur
        UIImage.imageWithData(nsData)
    } catch (e: Exception) {
        println("Error converting ImageBitmap to UIImage: ${e.message}")
        null
    }
}