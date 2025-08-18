package com.yusufteker.worthy.core.data.database.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.yusufteker.worthy.core.data.database.entities.RecurringFinancialItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringFinancialItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RecurringFinancialItemEntity)

    @Update
    suspend fun update(item: RecurringFinancialItemEntity)

    @Delete
    suspend fun delete(item: RecurringFinancialItemEntity)

    @Query("SELECT * FROM recurring_financial_item WHERE isIncome = :isIncome ORDER BY startDate")
    fun getAll(isIncome: Boolean): Flow<List<RecurringFinancialItemEntity>>

    @Query("SELECT * FROM recurring_financial_item ORDER BY startDate")
    fun getAll(): Flow<List<RecurringFinancialItemEntity>>

    @Query(
        """
        SELECT * FROM recurring_financial_item
        WHERE isIncome = :isIncome
          AND startDate <= :targetDate
          AND (endDate IS NULL OR endDate >= :targetDate)
    """
    )
    fun getItemsForMonth(
        isIncome: Boolean, targetDate: Int
    ): Flow<List<RecurringFinancialItemEntity>>

    @Query("DELETE FROM recurring_financial_item WHERE groupId = :groupId")
    suspend fun deleteByGroupId(groupId: String)

    @Query("SELECT * FROM recurring_financial_item WHERE groupId = :groupId")
    suspend fun getByGroupIdRaw(groupId: String): List<RecurringFinancialItemEntity>

    @Delete
    suspend fun deleteAll(items: List<RecurringFinancialItemEntity>)

    @Upsert
    suspend fun upsertAll(items: List<RecurringFinancialItemEntity>)

    @Query("SELECT * FROM recurring_financial_item WHERE groupId = :groupId ORDER BY startDate")
    fun getByGroupId(groupId: String): Flow<List<RecurringFinancialItemEntity>>

}
