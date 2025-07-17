package com.yusufteker.worthy.core.data.database.model

import androidx.room.*
import com.yusufteker.worthy.core.data.database.entities.WishlistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WishlistItemEntity): Long

    @Update
    suspend fun update(item: WishlistItemEntity)

    @Delete
    suspend fun delete(item: WishlistItemEntity)

    @Query("SELECT * FROM wishlist WHERE id = :id")
    suspend fun getById(id: Int): WishlistItemEntity?

    @Query("SELECT * FROM wishlist ORDER BY addedDate DESC")
    fun getAll(): Flow<List<WishlistItemEntity>>

    @Query("SELECT * FROM wishlist WHERE categoryId = :categoryId ORDER BY addedDate DESC")
    fun getByCategory(categoryId: Int): Flow<List<WishlistItemEntity>>
}
