package com.yusufteker.worthy.screen.wishlist.list.data.database.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistItemEntity
import com.yusufteker.worthy.screen.wishlist.list.data.database.relation.WishlistWithCategory
import kotlinx.coroutines.flow.Flow

// data/local/dao/WishlistItemDao.kt
@Dao
interface WishlistItemDao {

    @Transaction
    @Query("SELECT * FROM WISHLIST ORDER BY priority DESC, addedDate DESC")
    suspend fun getAllWithCategory(): List<WishlistWithCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WishlistItemEntity): Long

    @Update
    suspend fun update(item: WishlistItemEntity)

    @Delete
    suspend fun delete(item: WishlistItemEntity)

    @Query("SELECT * FROM WISHLIST WHERE id = :id")
    suspend fun getById(id: Int): WishlistItemEntity?

    @Transaction
    @Query("SELECT * FROM wishlist ORDER BY priority DESC")
    fun getAllWithCategoryFlow(): Flow<List<WishlistWithCategory>>

    @Transaction
    @Query("SELECT * FROM wishlist WHERE categoryId = :categoryId ORDER BY priority DESC")
    fun getByCategoryFlow(categoryId: Int): Flow<List<WishlistWithCategory>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?

}
