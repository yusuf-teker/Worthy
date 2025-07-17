package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.data.database.model.IncomeDao
import com.yusufteker.worthy.core.data.mappers.toDomain
import com.yusufteker.worthy.core.data.mappers.toEntity
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.repository.IncomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IncomeRepositoryImpl(
    private val incomeDao: IncomeDao
) : IncomeRepository {

    override fun getAll(): Flow<List<Income>> {
        return incomeDao.getAll().map { list -> list.map { it.toDomain() } }
    }


    override suspend fun getById(id: Int): Income? {
        return incomeDao.getById(id)?.toDomain()
    }

    override suspend fun insert(income: Income): Long {
        return incomeDao.insert(income.toEntity())
    }
    override suspend fun insertAll(incomes: List<Income>) {
        incomeDao.insertAll(incomes.map { it.toEntity() })
    }

    override suspend fun update(income: Income) {
        incomeDao.update(income.toEntity())
    }

    override suspend fun delete(income: Income) {
        incomeDao.delete(income.toEntity())
    }

    override fun getByCategory(categoryId: Int): Flow<List<Income>> {
        return incomeDao.getByCategory(categoryId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getFixed(): List<Income> {
        return incomeDao.getFixed().map { it.toDomain() }
    }
    override suspend fun deleteAll(items: List<Income>) {
        incomeDao.deleteAll(items.map { it.toEntity() })
    }

}
