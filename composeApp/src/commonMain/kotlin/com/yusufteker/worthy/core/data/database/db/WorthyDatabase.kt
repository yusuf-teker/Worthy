package com.yusufteker.worthy.core.data.database.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.yusufteker.worthy.core.data.converters.RoomTypeConverters
import com.yusufteker.worthy.core.data.database.model.CategoryDao
import com.yusufteker.worthy.core.data.database.model.ExpenseDao
import com.yusufteker.worthy.core.data.database.model.IncomeDao
import com.yusufteker.worthy.core.data.database.model.WishlistItemDao
import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.core.data.database.entities.ExpenseEntity
import com.yusufteker.worthy.core.data.database.entities.IncomeEntity
import com.yusufteker.worthy.core.data.database.entities.WishlistItemEntity

@Database(
    entities = [
        ExpenseEntity::class,
        IncomeEntity::class,
        WishlistItemEntity::class,
        CategoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
@ConstructedBy(WorthyDatabaseConstructor::class)
abstract class WorthyDatabase : RoomDatabase() {

    abstract val expenseDao: ExpenseDao
    abstract val incomeDao: IncomeDao
    abstract val wishlistDao: WishlistItemDao
    abstract val categoryDao: CategoryDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}

//Room arka planda oluşturuyor implementation
@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpect")
expect object WorthyDatabaseConstructor: RoomDatabaseConstructor<WorthyDatabase> {
    override fun initialize(): WorthyDatabase
}

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<WorthyDatabase>
}
