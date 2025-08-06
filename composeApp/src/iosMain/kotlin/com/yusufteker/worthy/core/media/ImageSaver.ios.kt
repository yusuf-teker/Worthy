package com.yusufteker.worthy.core.media

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.*
import platform.Foundation.*

actual class ImageSaver {
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun saveImage(bitmapData: ByteArray): String? {
        return try {
            // Documents directory'yi al
            val documentsDirectory = NSFileManager.defaultManager.URLsForDirectory(
                directory = NSDocumentDirectory,
                inDomains = NSUserDomainMask
            ).firstOrNull() as? NSURL

            if (documentsDirectory == null) {
                println("Documents directory bulunamadı")
                return null
            }

            // Dosya adı oluştur
            val fileName = "wishlist_${NSDate().timeIntervalSince1970.toLong()}.png"
            val fileURL = documentsDirectory.URLByAppendingPathComponent(fileName)

            if (fileURL == null) {
                println("File URL oluşturulamadı")
                return null
            }

            // ByteArray'i NSData'ya çevir
            val nsData = bitmapData.usePinned { pinned ->
                NSData.create(bytes = pinned.addressOf(0), length = bitmapData.size.toULong())
            }

            // Dosyaya yaz
            val success = nsData.writeToURL(
                url = fileURL,
                atomically = true
            )

            if (success) {
                fileURL.path // Dosya yolunu döndür
            } else {
                println("Dosya yazma başarısız")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}