package com.yusufteker.worthy.screen.wishlist.list.data.database.repository

import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.screen.wishlist.list.data.database.model.WishlistItemDao
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WishlistRepositoryImpl(
    private val itemDao: WishlistItemDao,
    private val transactionRepository: TransactionRepository
) : WishlistRepository {

    override fun getAll(): Flow<List<WishlistItem>> =
        itemDao.getAllWithCategoryFlow()
            .map { list -> list.map { it.toDomain() } }

    override fun getByCategory(categoryId: Int): Flow<List<WishlistItem>> =
        itemDao.getByCategoryFlow(categoryId)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Int): WishlistItem? {
        val entity = itemDao.getById(id)
        val category = entity?.categoryId?.let { itemDao.getCategoryById(it) }
        return entity?.toDomain(category?.toDomain())
    }

    override suspend fun insert(item: WishlistItem): Long {
        return itemDao.insert(item.toEntity())
    }

    override suspend fun update(item: WishlistItem) {
        itemDao.update(item.toEntity())
    }

    override suspend fun delete(item: WishlistItem) {
        itemDao.delete(item.toEntity())
    }
    override suspend fun deleteById(id: Int) {
        itemDao.deleteById(id)
    }
    override suspend fun updateIsPurchased(itemId: Int, isPurchased: Boolean, purchasedTime: Long?) {
        itemDao.updateIsPurchased(itemId, isPurchased, purchasedTime)
    }

    override fun searchWithCategory(query: String): Flow<List<WishlistItem>> =
        itemDao.searchWithCategory("%$query%").map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun saveExpense(expense: Transaction) {
        transactionRepository.insert(expense)
    }

    override suspend fun deleteExpense(expense: Transaction) {
        transactionRepository.delete(expense)
    }

}
