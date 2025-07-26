package com.yusufteker.worthy.core.media

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

import androidx.compose.ui.graphics.toPixelMap
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.UIKit.*
import androidx.compose.ui.graphics.asSkiaBitmap


actual typealias ImageFormat = EncodedImageFormat

/*
🎨 Compose ve Skia
Kotlin Multiplatform (KMP) projelerinde JetBrains'in Compose UI (desktop/iOS tarafında), alt seviyede grafik işlemleri için Skia’yı kullanır.

Android tarafında Compose doğrudan Android’in Canvas/Bitmap API’siyle çalışır. Ama iOS ve Desktop tarafında Skia kullanır.
*/

actual fun ImageBitmap.toByteArray(
    format: ImageFormat
): ByteArray {
    val data = Image
        // Convert ImageBitmap to Skia Image
        .makeFromBitmap(asSkiaBitmap())
        // Encode the Skia Image to the specified format (PNG)
        .encodeToData(format) ?: error("This painter cannot be encoded to $format")

    // Convert the Skia Data to a ByteArray
    return data.bytes
}