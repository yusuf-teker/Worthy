package com.yusufteker.worthy.screen.wishlist.list.domain

import kotlinx.coroutines.flow.Flow

interface WishlistCategoryRepository {
    fun getAll(): Flow<List<WishlistCategory>>
    suspend fun insert(category: WishlistCategory): Long
    suspend fun update(category: WishlistCategory)
    suspend fun delete(category: WishlistCategory)
    suspend fun getById(id: Int): WishlistCategory?
}
