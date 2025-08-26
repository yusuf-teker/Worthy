package com.yusufteker.worthy.core.domain.repository

import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.RecurringItem
import kotlinx.coroutines.flow.Flow

interface RecurringFinancialItemRepository {
    suspend fun add(item: RecurringItem.Generic)
    suspend fun update(item: RecurringItem.Generic)
    suspend fun delete(item: RecurringItem.Generic)
    fun getAll(isIncome: Boolean): Flow<List<RecurringItem.Generic>>

    fun getAll(): Flow<List<RecurringItem.Generic>>
    fun getForMonth(isIncome: Boolean, date: AppDate): Flow<List<RecurringItem.Generic>>

    suspend fun deleteGroup(groupId: String)

    suspend fun updateGroup(items: List<RecurringItem.Generic>)
}