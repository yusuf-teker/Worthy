package com.yusufteker.worthy.screen.card.data.database.repository

import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.screen.card.data.database.model.CardDao
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardRepositoryImpl(private val cardDao: CardDao) : CardRepository {
    override  fun getAll(): Flow<List<Card>> {
        return cardDao.getAllCards().map { entityList -> entityList.map { it.toDomain() } }
    }

    override suspend fun addCard(card: Card) {
        cardDao.insertCard(card.toEntity())
    }

    override suspend fun deleteCard(cardId: Int) {
        cardDao.deleteCardById(cardId)
    }

    override suspend fun updateCard(card: Card) {
        cardDao.updateCard(card.toEntity())
    }

    override fun getCardById(cardId: Int): Flow<Card?> {
        return cardDao.getCardById(cardId).map { it?.toDomain()}
    }

}