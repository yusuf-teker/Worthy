package com.yusufteker.worthy.screen.wishlist.list.domain

import com.yusufteker.worthy.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getAll(): Flow<List<WishlistItem>>
    fun getByCategory(categoryId: Int): Flow<List<WishlistItem>>

    suspend fun getById(id: Int): WishlistItem?
    suspend fun insert(item: WishlistItem): Long
    suspend fun update(item: WishlistItem)
    suspend fun delete(item: WishlistItem)
    suspend fun deleteById(id: Int)
    suspend fun updateIsPurchased(itemId: Int, isPurchased: Boolean, purchasedTime: Long?)

    fun searchWithCategory(query: String): Flow<List<WishlistItem>>

    suspend fun saveExpense(expense: Transaction)
    suspend fun deleteExpense(expense: Transaction)

}