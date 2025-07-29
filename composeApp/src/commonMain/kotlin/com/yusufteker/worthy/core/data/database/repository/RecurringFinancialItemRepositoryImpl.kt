package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.data.database.model.RecurringFinancialItemDao
import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem
import com.yusufteker.worthy.core.domain.repository.RecurringFinancialItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecurringFinancialItemRepositoryImpl(
    private val dao: RecurringFinancialItemDao
): RecurringFinancialItemRepository {
    override suspend fun add(item: RecurringFinancialItem) {
        dao.insert(item.toEntity())
    }

    override suspend fun update(item: RecurringFinancialItem) {
        dao.update(item.toEntity())
    }

    override suspend fun delete(item: RecurringFinancialItem) {
        dao.delete(item.toEntity())
    }

    override fun getAll(isIncome: Boolean) =
        dao.getAll(isIncome).map { list -> list.map { it.toDomain() } }

    override fun getAll() =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getForMonth(isIncome: Boolean, month: Int, year: Int): Flow<List<RecurringFinancialItem>> {
        return dao.getItemsForMonth(isIncome, month, year).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun deleteGroup(groupId: String) {
        dao.deleteByGroupId(groupId)
    }

    override suspend fun updateGroup(items: List<RecurringFinancialItem>) {
        if (items.isEmpty()) return

        val groupId = items.first().groupId
        val currentEntities = dao.getByGroupIdRaw(groupId) // suspend, entity listesi
        val currentIds = currentEntities.map { it.id }.toSet()
        val newIds = items.map { it.id }.toSet()

        val toDelete = currentEntities.filter { it.id !in newIds }
        val toUpsert = items.map { it.toEntity() }

        dao.deleteAll(toDelete)
        dao.upsertAll(toUpsert)
    }


}
