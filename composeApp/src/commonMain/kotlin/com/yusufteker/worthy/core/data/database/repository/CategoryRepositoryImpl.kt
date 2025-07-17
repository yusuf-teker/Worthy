package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.data.database.model.CategoryDao
import com.yusufteker.worthy.core.data.mappers.toDomain
import com.yusufteker.worthy.core.data.mappers.toEntity
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAll(): Flow<List<Category>> {
        return categoryDao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override fun getByType(type: CategoryType): Flow<List<Category>> {
        return categoryDao.getByType(type).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getById(id: Int): Category? {
        return categoryDao.getById(id)?.toDomain()
    }

    override suspend fun insert(category: Category): Long {
        return categoryDao.insert(category.toEntity())
    }

    override suspend fun update(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override suspend fun delete(category: Category) {
        categoryDao.delete(category.toEntity())
    }
}
