package com.yusufteker.worthy.core.domain.repository

import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TransactionRepository {

    fun getAll(): Flow<List<Transaction>>

    fun getExpenseCategories(): Flow<List<Category>>

    fun getIncomeCategories(): Flow<List<Category>>

    fun getCards(): Flow<List<Card>>

    fun getByCategory(categoryId: Int): Flow<List<Transaction>>

    fun getByCardId(cardId: Int): Flow<List<Transaction>>

    fun getByCardIdConverted(cardId: Int): Flow<List<Transaction>>

    fun getByType(type: TransactionType): Flow<List<Transaction>>

    suspend fun getById(id: Int): Transaction?

    suspend fun getByIdWithRefund(id: Int): List<Transaction>?

    suspend fun insert(transaction: Transaction): Long

    suspend fun insertAll(transactions: List<Transaction>)

    suspend fun update(transaction: Transaction)

    suspend fun delete(transaction: Transaction)

    suspend fun deleteById(transactionId: Int)

    suspend fun deleteByOriginalId(originalId: Int)
    suspend fun deleteAll(items: List<Transaction>)

    fun getTransactionsSince(startDate: LocalDate): Flow<List<Transaction>>

    fun getTransactionsSince(startDate: LocalDate, transactionType: TransactionType): Flow<List<Transaction>>
    fun getIncomeTransactionsSince(startDate: LocalDate): Flow<List<Transaction>>

    fun getRelatedTransactions(relatedTransactionId: Int): Flow<List<Transaction>>

    suspend fun refundTransaction(transaction: Transaction): Long
}
