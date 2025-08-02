package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.data.database.model.ExpenseDao
import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.repository.ExpenseRepository
import com.yusufteker.worthy.core.domain.toEpochMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlin.collections.map
import kotlin.time.ExperimentalTime

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override fun getAll(): Flow<List<Expense>> {
        return expenseDao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override fun getByCategory(categoryId: Int): Flow<List<Expense>> {
        return expenseDao.getByCategory(categoryId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getById(id: Int): Expense? {
        return expenseDao.getById(id)?.toDomain()
    }

    override suspend fun insert(expense: Expense): Long {
        return expenseDao.insert(expense.toEntity())
    }

    override suspend fun insertAll(expenses: List<Expense>) {
        expenseDao.insertAll(expenses.map { it.toEntity() })
    }

    override suspend fun update(expense: Expense) {
        expenseDao.update(expense.toEntity())
    }

    override suspend fun delete(expense: Expense) {
        expenseDao.delete(expense.toEntity())
    }

    override suspend fun getFixed(): List<Expense> {
        return expenseDao.getFixed().map { it.toDomain() }
    }

    override suspend fun deleteAll(items: List<Expense>) {
        expenseDao.deleteAll(items.map { it.toEntity() })
    }

    @OptIn(ExperimentalTime::class)
    override fun getExpensesSince(startDate: LocalDate): Flow<List<Expense>> {
        val startMillis = startDate.toEpochMillis()
        return expenseDao.getExpensesFrom(startMillis)
            .map { entities -> entities.map { it.toDomain() } }
    }

}