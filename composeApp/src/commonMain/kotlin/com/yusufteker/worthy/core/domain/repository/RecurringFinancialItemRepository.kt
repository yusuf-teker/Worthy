package com.yusufteker.worthy.core.domain.repository

import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem
import kotlinx.coroutines.flow.Flow

interface RecurringFinancialItemRepository {
    suspend fun add(item: RecurringFinancialItem)
    suspend fun update(item: RecurringFinancialItem)
    suspend fun delete(item: RecurringFinancialItem)
     fun getAll(isIncome: Boolean):  Flow<List<RecurringFinancialItem>>

    fun getAll(): Flow<List<RecurringFinancialItem>>
     fun getForMonth(isIncome: Boolean,date: AppDate): Flow<List<RecurringFinancialItem>>

    suspend fun deleteGroup(groupId: String)

    suspend fun updateGroup(items: List<RecurringFinancialItem>)
}