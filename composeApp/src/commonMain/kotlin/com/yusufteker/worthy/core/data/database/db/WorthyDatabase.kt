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
import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.core.data.database.entities.ExpenseEntity
import com.yusufteker.worthy.core.data.database.entities.IncomeEntity
import com.yusufteker.worthy.core.data.database.entities.RecurringFinancialItemEntity
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistItemEntity
import com.yusufteker.worthy.core.data.database.model.RecurringFinancialItemDao
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistCategoryEntity
import com.yusufteker.worthy.screen.wishlist.list.data.database.model.WishlistCategoryDao
import com.yusufteker.worthy.screen.wishlist.list.data.database.model.WishlistItemDao

@Database(
    entities = [
        ExpenseEntity::class,
        IncomeEntity::class,
        WishlistItemEntity::class,
        CategoryEntity::class,
        RecurringFinancialItemEntity::class,
        WishlistCategoryEntity::class,


    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
@ConstructedBy(WorthyDatabaseConstructor::class)
abstract class WorthyDatabase : RoomDatabase() {

    abstract val expenseDao: ExpenseDao
    abstract val incomeDao: IncomeDao
    abstract val categoryDao: CategoryDao

    abstract val wishlistItemDao: WishlistItemDao
    abstract val wishlistCategoryDao: WishlistCategoryDao

    abstract val recurringFinancialItemDao: RecurringFinancialItemDao

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
