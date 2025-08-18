package com.yusufteker.worthy.core.media

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream


actual typealias ImageFormat = Bitmap.CompressFormat

actual fun ImageBitmap.toByteArray(
    format: ImageFormat
): ByteArray {

    return ByteArrayOutputStream().use {
        asAndroidBitmap().compress(format, 100, it)
        it.toByteArray()
    }
}

actual fun PlatformImage.toByteArray(format: ImageFormat): ByteArray {
    return this.bitmap.toByteArray()
}