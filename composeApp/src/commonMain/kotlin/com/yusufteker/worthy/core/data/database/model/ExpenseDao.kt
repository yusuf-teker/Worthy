package com.yusufteker.worthy.core.data.database.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yusufteker.worthy.core.data.database.entities.ExpenseEntity
import com.yusufteker.worthy.core.data.database.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<ExpenseEntity>)

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getById(id: Int): ExpenseEntity?

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getByCategory(categoryId: Int): Flow<List<ExpenseEntity>>



    @Query("SELECT * FROM expenses WHERE isFixed = 1")
    suspend fun getFixed(): List<ExpenseEntity>

    @Delete
    suspend fun deleteAll(items: List<ExpenseEntity>)

    @Query("SELECT * FROM expenses WHERE date >= :startDate ORDER BY date DESC")
    fun getExpensesFrom(startDate: Long): Flow<List<ExpenseEntity>>


}