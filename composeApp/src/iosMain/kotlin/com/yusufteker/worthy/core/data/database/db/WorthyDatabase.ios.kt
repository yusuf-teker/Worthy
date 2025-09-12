package com.yusufteker.worthy.core.data.database.db

import androidx.room.Room
import androidx.room.RoomDatabase
import com.yusufteker.worthy.core.data.database.migrations.MIGRATION_2_3
import com.yusufteker.worthy.core.data.database.migrations.MIGRATION_3_4
import com.yusufteker.worthy.core.data.database.migrations.migrationList
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<WorthyDatabase> {
        val dbFile = documentDirectory() + "/${WorthyDatabase.DATABASE_NAME}}"
        return Room.databaseBuilder<WorthyDatabase>(
            name = dbFile
        ).addMigrations(*migrationList.toTypedArray() )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String{
        val documentDirectory = NSFileManager.defaultManager().URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull( documentDirectory?.path() )
    }
}