package com.yusufteker.worthy.screen.wishlist.list.data.database.model

import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistCategory
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WishlistCategoryRepositoryImpl(
    private val categoryDao: WishlistCategoryDao
) : WishlistCategoryRepository {

    override fun getAll(): Flow<List<WishlistCategory>> =
        categoryDao.getAll()
            .map { it.map { entity -> entity.toDomain() } }

    override suspend fun insert(category: WishlistCategory): Long =
        categoryDao.insert(category.toEntity())

    override suspend fun update(category: WishlistCategory) =
        categoryDao.update(category.toEntity())

    override suspend fun delete(category: WishlistCategory) =
        categoryDao.delete(category.toEntity())

    override suspend fun getById(id: Int): WishlistCategory? =
        categoryDao.getById(id)?.toDomain()
}
