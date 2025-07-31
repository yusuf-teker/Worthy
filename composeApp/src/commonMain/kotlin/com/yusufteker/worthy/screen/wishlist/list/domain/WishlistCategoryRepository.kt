package com.yusufteker.worthy.screen.wishlist.list.domain

import com.yusufteker.worthy.core.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface WishlistCategoryRepository {
    fun getAll(): Flow<List<Category>>
    suspend fun insert(category: Category): Long
    suspend fun update(category: Category)
    suspend fun delete(category: Category)
    suspend fun getById(id: Int): Category?
}
