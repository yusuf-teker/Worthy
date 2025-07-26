package com.yusufteker.worthy.core.media

import android.content.Context
import java.io.File
import java.io.FileOutputStream

actual class ImageSaver(private val context: Context) {
    actual suspend fun saveImage(bitmapData: ByteArray): String? {
        return try {
            //Dosya oluştur
            val file = File(context.filesDir, "wishlist_${System.currentTimeMillis()}.png")
            // Dosyaya byteArrayi yaz
            // use dosya işlemlerinde dosya kapatmayı otomatik yapar
            FileOutputStream(file).use { it.write(bitmapData) }
            // Dosya yolunu döndür
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}