package com.yusufteker.worthy.core.domain.repository

import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface ExpenseRepository {
    fun getAll(): Flow<List<Expense>>
    fun getByCategory(categoryId: Int): Flow<List<Expense>>
    suspend fun getById(id: Int): Expense?
    suspend fun insert(expense: Expense): Long
    suspend fun insertAll(expenses: List<Expense>)
    suspend fun update(expense: Expense)
    suspend fun delete(expense: Expense)
    suspend fun getFixed(): List<Expense>
    suspend fun deleteAll(items: List<Expense>)

    fun getExpensesSince(startDate: LocalDate): Flow<List<Expense>>

}