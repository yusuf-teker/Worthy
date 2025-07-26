package com.yusufteker.worthy.screen.wishlist.list.data.database.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistCategoryEntity
import kotlinx.coroutines.flow.Flow

// data/local/dao/WishlistCategoryDao.kt
@Dao
interface WishlistCategoryDao {

    @Query("SELECT * FROM wishlist_categories ORDER BY name ASC")
    fun getAll(): Flow<List<WishlistCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: WishlistCategoryEntity): Long

    @Update
    suspend fun update(category: WishlistCategoryEntity)

    @Delete
    suspend fun delete(category: WishlistCategoryEntity)

    @Query("SELECT * FROM wishlist_categories WHERE id = :id")
    suspend fun getById(id: Int): WishlistCategoryEntity?
}
