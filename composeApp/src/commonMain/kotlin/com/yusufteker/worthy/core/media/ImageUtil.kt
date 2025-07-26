package com.yusufteker.worthy.core.media

import androidx.compose.ui.graphics.ImageBitmap


expect enum class ImageFormat {
    PNG, JPEG, WEBP
}

/**
 * Converts [ImageBitmap] to image with desired [format] and returns its bytes.
 * */
expect fun ImageBitmap.toByteArray(format: ImageFormat = ImageFormat.PNG) : ByteArray
