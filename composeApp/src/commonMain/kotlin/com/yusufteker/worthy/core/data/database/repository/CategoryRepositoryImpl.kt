package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.data.database.entities.defaultCategoryEntities
import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.data.database.model.CategoryDao
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val cardRepository: CardRepository
) : CategoryRepository {

    override suspend fun initializeDefaultCategories() {
        if (categoryDao.getCategoryCount() == 0) {
            categoryDao.insertAll(defaultCategoryEntities)
        }
    }

    override fun getAll(): Flow<List<Category>> = flow {
        val currentList = categoryDao.getAllOnce()

        if (currentList.isEmpty()) {
            initializeDefaultCategories()
            val defaultList = categoryDao.getAllOnce()
            emit(defaultList.map { it.toDomain() })
        } else {
            emit(currentList.map { it.toDomain() })
        }
    }
    override fun getCards(): Flow<List<Card>> {
        return cardRepository.getAll()
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

    override suspend fun insertAll(categories: List<Category>) {
        categoryDao.insertAll(categories.map { it.toEntity() })
    }

    override suspend fun update(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override suspend fun delete(category: Category) {
        categoryDao.delete(category.toEntity())
    }
}
