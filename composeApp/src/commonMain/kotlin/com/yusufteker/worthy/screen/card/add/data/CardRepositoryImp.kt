package com.yusufteker.worthy.screen.card.add.data

import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.data.database.model.CardDao
import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.screen.card.add.domain.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardRepositoryImpl(private val cardDao: CardDao) : CardRepository {
    override  fun getCards(): Flow<List<Card>> {
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

}