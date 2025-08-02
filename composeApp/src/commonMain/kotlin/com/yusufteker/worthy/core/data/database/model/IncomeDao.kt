package com.yusufteker.worthy.core.data.database.model

import androidx.room.*
import com.yusufteker.worthy.core.data.database.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(income: IncomeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(incomes: List<IncomeEntity>)

    @Update
    suspend fun update(income: IncomeEntity)

    @Delete
    suspend fun delete(income: IncomeEntity)

    @Query("SELECT * FROM incomes WHERE id = :id")
    suspend fun getById(id: Int): IncomeEntity?

    @Query("SELECT * FROM incomes ORDER BY date DESC")
    fun getAll(): Flow<List<IncomeEntity>>


    @Query("SELECT * FROM incomes WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getByCategory(categoryId: Int): Flow<List<IncomeEntity>>

    @Query("SELECT * FROM incomes WHERE isFixed = true")
    suspend fun getFixed(): List<IncomeEntity>
    @Delete
    suspend fun deleteAll(items: List<IncomeEntity>)

    @Query("SELECT * FROM incomes WHERE date >= :startDate ORDER BY date DESC")
    fun getIncomesFrom(startDate: Long): Flow<List<IncomeEntity>>

}
