package com.yusufteker.worthy.screen.card.domain.repository

import com.yusufteker.worthy.screen.card.domain.model.Card
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun getAll(): Flow<List<Card>>
    suspend fun addCard(card: Card)
    suspend fun deleteCard(cardId: Int)
    suspend fun updateCard(card: Card)

     fun getCardById(cardId: Int): Flow<Card?>


}