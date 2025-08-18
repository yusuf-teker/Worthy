package com.yusufteker.worthy.core.data.database.model


import androidx.room.*
import com.yusufteker.worthy.core.data.database.entities.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardEntity): Long

    @Update
    suspend fun updateCard(card: CardEntity)

    @Delete
    suspend fun deleteCard(card: CardEntity)

    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun deleteCardById(id: Int)
    @Query("SELECT * FROM cards WHERE id = :id LIMIT 1")
    suspend fun getCardById(id: Int): CardEntity?

    @Query("SELECT * FROM cards ORDER BY expiryYear DESC, expiryMonth DESC")
    fun getAllCards(): Flow<List<CardEntity>>

    @Query("DELETE FROM cards")
    suspend fun deleteAllCards()
}
