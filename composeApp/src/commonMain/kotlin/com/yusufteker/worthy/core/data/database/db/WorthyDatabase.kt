package com.yusufteker.worthy.core.data.database.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.yusufteker.worthy.core.data.database.converters.RoomTypeConverters
import com.yusufteker.worthy.screen.card.data.database.entities.CardEntity
import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.core.data.database.entities.RecurringFinancialItemEntity
import com.yusufteker.worthy.core.data.database.entities.TransactionEntity
import com.yusufteker.worthy.screen.card.data.database.model.CardDao
import com.yusufteker.worthy.core.data.database.model.CategoryDao
import com.yusufteker.worthy.core.data.database.model.RecurringFinancialItemDao
import com.yusufteker.worthy.core.data.database.model.TransactionDao
import com.yusufteker.worthy.screen.subscription.data.database.entities.SubscriptionEntity
import com.yusufteker.worthy.screen.subscription.data.database.model.SubscriptionDao
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistItemEntity
import com.yusufteker.worthy.screen.wishlist.list.data.database.model.WishlistItemDao

@Database(
    entities = [
        WishlistItemEntity::class,
        CategoryEntity::class,
        RecurringFinancialItemEntity::class,
        TransactionEntity::class,
        CardEntity::class,
        SubscriptionEntity::class
    ], version = 2, exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
@ConstructedBy(WorthyDatabaseConstructor::class)
abstract class WorthyDatabase : RoomDatabase() {

    abstract val categoryDao: CategoryDao

    abstract val wishlistItemDao: WishlistItemDao

    abstract val recurringFinancialItemDao: RecurringFinancialItemDao

    abstract val transactionDao: TransactionDao

    abstract val cardDao: CardDao

    abstract val subscriptionDao: SubscriptionDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}

//Room arka planda oluşturuyor implementation
@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpect")
expect object WorthyDatabaseConstructor : RoomDatabaseConstructor<WorthyDatabase> {
    override fun initialize(): WorthyDatabase
}

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<WorthyDatabase>
}
