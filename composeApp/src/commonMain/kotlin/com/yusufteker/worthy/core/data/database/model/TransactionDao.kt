package com.yusufteker.worthy.core.data.database.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yusufteker.worthy.core.data.database.entities.TransactionEntity
import com.yusufteker.worthy.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY transactionDate DESC")
    fun getByCategory(categoryId: Int): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE cardId = :cardId ORDER BY transactionDate DESC")
    fun getByCard(cardId: Int): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE transactionType = :type ORDER BY transactionDate DESC")
    fun getByType(type: TransactionType): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id OR relatedTransactionId = :id")
    suspend fun getById(id: Int): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE id = :id OR relatedTransactionId = :id")
    suspend fun getByIdWithRefund(id: Int): List<TransactionEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    // update original id
    @Query("UPDATE transactions SET originalId = :id WHERE id = :id")
    suspend fun updateOriginalId(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteById(transactionId: Int)

    // delete by original ıd
    @Query(
        """
    DELETE FROM transactions 
    WHERE originalId = :originalId OR relatedTransactionId = :originalId
"""
    )
    suspend fun deleteByOriginalId(originalId: Int)

    @Delete
    suspend fun deleteAll(items: List<TransactionEntity>)

    @Query("SELECT * FROM transactions WHERE transactionDate >= :startDate ORDER BY transactionDate DESC")
    fun getTransactionsFrom(startDate: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE transactionDate >= :startDate AND transactionType = :transactionType ORDER BY transactionDate DESC")
    fun getTransactionsFrom(
        startDate: Long, transactionType: TransactionType
    ): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE relatedTransactionId = :relatedTransactionId ORDER BY transactionDate DESC")
    fun getRelatedTransactions(relatedTransactionId: Int): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun refundTransaction(transaction: TransactionEntity): Long

    @Query(
        """
        SELECT t.* FROM transactions t
        LEFT JOIN transactions refund
        ON refund.relatedTransactionId = t.id AND refund.transactionType = 'REFUND'
        WHERE t.installmentCount IS NOT NULL
        AND (refund.id IS NULL OR refund.id = t.id)
        GROUP BY COALESCE(refund.id, t.id)
        ORDER BY t.transactionDate DESC
    """
    )
    fun getAllInstallments(): Flow<List<TransactionEntity>> // Refund var ise normali getirme sadece refundu al
}
