package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.data.database.model.IncomeDao
import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.repository.IncomeRepository
import com.yusufteker.worthy.core.domain.toEpochMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.ExperimentalTime

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

    @OptIn(ExperimentalTime::class)
    override fun getIncomesSince(startDate: LocalDate): Flow<List<Income>> {
        val startMillis = startDate.toEpochMillis()
        return incomeDao.getIncomesFrom(startMillis)
            .map { entities -> entities.map { it.toDomain() } }
    }

}
