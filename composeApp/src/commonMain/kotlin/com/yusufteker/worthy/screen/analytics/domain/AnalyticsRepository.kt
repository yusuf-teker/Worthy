package com.yusufteker.worthy.screen.analytics.domain

import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {

    fun getTransactions(): Flow<List<Transaction>>

    fun getCategories(): Flow<List<Category>>

    fun getCards(): Flow<List<Card>>

    suspend fun deleteTransaction(id: Int)

    fun getUserPrefCurrency(): Flow<Currency>

}