package com.yusufteker.worthy.core.media

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun rememberImagePicker(): ImagePicker

expect suspend fun loadImageBitmapFromPath(path: String): ImageBitmap?

expect class PlatformImage

expect fun PlatformImage.toImageBitmap(): ImageBitmap

/**
 * Platform-specific image picker interface
 */
interface ImagePicker {
    /**
     * Pick image from gallery
     * @param onResult callback with selected ImageBitmap or null if cancelled
     */
    fun pickFromGallery(onResult: (PlatformImage?) -> Unit)

    /**
     * Take photo with camera
     * @param onResult callback with captured ImageBitmap or null if cancelled
     */
    fun pickFromCamera(onResult: (PlatformImage?) -> Unit)

    /**
     * Check if camera is available on device
     */
    fun isCameraAvailable(): Boolean

    /**
     * Request necessary permissions (mainly for camera)
     * Gallery permissions handled automatically by PickVisualMedia
     * @param onResult callback with permission granted status
     */
    fun requestCameraPermission(onResult: (Boolean) -> Unit)

    fun cropImage(
        image: PlatformImage,
        aspectRatio: AspectRatio = AspectRatio.SQUARE,
        onCropped: (PlatformImage?) -> Unit
    )

    fun cancelPendingCallbacks()

}

enum class AspectRatio(val widthRatio: Float, val heightRatio: Float) {
    SQUARE(1f, 1f), RATIO_16x9(16f, 9f), RATIO_4x3(4f, 3f), RATIO_3x2(3f, 2f);

    fun toFloat(): Float = widthRatio.toFloat() / heightRatio
}
