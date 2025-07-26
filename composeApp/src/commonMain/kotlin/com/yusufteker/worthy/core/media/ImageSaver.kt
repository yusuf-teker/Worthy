package com.yusufteker.worthy.core.media


expect class ImageSaver {
    suspend fun saveImage(bitmapData: ByteArray): String? // Returns file path or URI
}


