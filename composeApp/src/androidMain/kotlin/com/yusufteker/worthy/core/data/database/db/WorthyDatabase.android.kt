package com.yusufteker.worthy.core.data.database.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<WorthyDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(WorthyDatabase.DATABASE_NAME)

        return Room.databaseBuilder(
            context,
            dbFile.absolutePath)
    }
}