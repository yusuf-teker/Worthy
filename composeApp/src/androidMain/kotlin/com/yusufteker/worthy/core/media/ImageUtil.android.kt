package com.yusufteker.worthy.core.media

import androidx.compose.ui.graphics.ImageBitmap

import androidx.compose.ui.graphics.asAndroidBitmap
import android.graphics.Bitmap
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