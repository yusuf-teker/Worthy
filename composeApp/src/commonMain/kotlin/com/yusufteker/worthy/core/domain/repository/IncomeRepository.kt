package com.yusufteker.worthy.core.domain.repository

import com.yusufteker.worthy.core.domain.model.Income
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface IncomeRepository {
    fun getAll(): Flow<List<Income>>
    fun getByCategory(categoryId: Int): Flow<List<Income>>
    suspend fun getById(id: Int): Income?
    suspend fun insert(income: Income): Long

    suspend fun insertAll(incomes: List<Income>)

    suspend fun update(income: Income)
    suspend fun delete(income: Income)

    suspend fun getFixed(): List<Income>
    suspend fun deleteAll(items: List<Income>)

    fun getIncomesSince(startDate: LocalDate): Flow<List<Income>>

}