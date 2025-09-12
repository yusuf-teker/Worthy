package com.yusufteker.worthy.core.data.database.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yusufteker.worthy.core.data.database.migrations.MIGRATION_2_3
import com.yusufteker.worthy.core.data.database.migrations.MIGRATION_3_4
import com.yusufteker.worthy.core.data.database.migrations.migrationList

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<WorthyDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(WorthyDatabase.DATABASE_NAME)

        return Room.databaseBuilder(
            context,
            WorthyDatabase::class.java,
            dbFile.absolutePath
        ).addMigrations(*migrationList.toTypedArray())
    }
}