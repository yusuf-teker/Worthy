package com.yusufteker.worthy.core.data.database.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yusufteker.worthy.core.data.database.entities.defaultCategoryEntities
import com.yusufteker.worthy.core.domain.model.defaultCategories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<WorthyDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(WorthyDatabase.DATABASE_NAME)

        return Room.databaseBuilder(
            context,
            WorthyDatabase::class.java,
            dbFile.absolutePath)
    }
}