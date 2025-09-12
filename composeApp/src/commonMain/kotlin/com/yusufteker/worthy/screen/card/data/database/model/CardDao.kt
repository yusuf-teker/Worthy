package com.yusufteker.worthy.screen.card.data.database.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yusufteker.worthy.screen.card.data.database.entities.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCard(card: CardEntity): Long

    @Update
    suspend fun updateCard(card: CardEntity)

    @Delete
    suspend fun deleteCard(card: CardEntity)

    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun deleteCardById(id: Int)

    @Query("SELECT * FROM cards WHERE id = :id LIMIT 1")
     fun getCardById(id: Int): Flow<CardEntity?>

    @Query("SELECT * FROM cards")
    fun getAllCards(): Flow<List<CardEntity>>

    @Query("DELETE FROM cards")
    suspend fun deleteAllCards()
}