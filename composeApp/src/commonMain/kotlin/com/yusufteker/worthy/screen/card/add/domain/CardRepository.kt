package com.yusufteker.worthy.screen.card.add.domain

import com.yusufteker.worthy.core.domain.model.Card
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    suspend fun getCards(): Flow<List<Card>>
    suspend fun addCard(card: Card)
    suspend fun deleteCard(cardId: Int)
    suspend fun updateCard(card: Card)
}