package com.yusufteker.worthy.core.domain.repository

import com.yusufteker.worthy.core.domain.model.WishlistItem
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getAll(): Flow<List<WishlistItem>>
    fun getByCategory(categoryId: Int): Flow<List<WishlistItem>>
    suspend fun getById(id: Int): WishlistItem?
    suspend fun insert(item: WishlistItem): Long
    suspend fun update(item: WishlistItem)
    suspend fun delete(item: WishlistItem)
}