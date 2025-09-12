package com.yusufteker.worthy.core.domain.repository

import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.screen.card.domain.model.Card
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAll(): Flow<List<Category>>

    fun getCards(): Flow<List<Card>>

    suspend fun initializeDefaultCategories()
    fun getByType(type: CategoryType): Flow<List<Category>>
    suspend fun getById(id: Int): Category?
    suspend fun insert(category: Category): Long
    suspend fun insertAll(categories: List<Category>)
    suspend fun update(category: Category)
    suspend fun delete(category: Category)
}