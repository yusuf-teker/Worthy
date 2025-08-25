package com.yusufteker.worthy.screen.subscription.data.database.model


import androidx.room.*
import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.screen.subscription.data.database.entities.SubscriptionCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: SubscriptionCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<SubscriptionCategoryEntity>)
    @Query("SELECT COUNT(*) FROM subscription_categories")
    suspend fun getCategoryCount(): Int

    @Update
    suspend fun update(category: SubscriptionCategoryEntity)

    @Delete
    suspend fun delete(category: SubscriptionCategoryEntity)

    @Query("SELECT * FROM subscription_categories ORDER BY name ASC")
    fun getAll(): Flow<List<SubscriptionCategoryEntity>>

    @Query("SELECT * FROM subscription_categories WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): SubscriptionCategoryEntity?

    @Query("SELECT * FROM subscription_categories WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): SubscriptionCategoryEntity?
}
