package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.data.database.model.WishlistItemDao
import com.yusufteker.worthy.core.data.mappers.toDomain
import com.yusufteker.worthy.core.data.mappers.toEntity
import com.yusufteker.worthy.core.domain.model.WishlistItem
import com.yusufteker.worthy.core.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WishlistRepositoryImpl(
    private val wishlistDao: WishlistItemDao
) : WishlistRepository {

    override fun getAll(): Flow<List<WishlistItem>> {
        return wishlistDao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override fun getByCategory(categoryId: Int): Flow<List<WishlistItem>> {
        return wishlistDao.getByCategory(categoryId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getById(id: Int): WishlistItem? {
        return wishlistDao.getById(id)?.toDomain()
    }

    override suspend fun insert(item: WishlistItem): Long {
        return wishlistDao.insert(item.toEntity())
    }

    override suspend fun update(item: WishlistItem) {
        wishlistDao.update(item.toEntity())
    }

    override suspend fun delete(item: WishlistItem) {
        wishlistDao.delete(item.toEntity())
    }
}
