package com.yusufteker.worthy.screen.subscription.data.database.repository

import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.getCurrentLocalDateTime
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.toRoomInt
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import com.yusufteker.worthy.screen.subscription.data.database.model.SubscriptionDao
import com.yusufteker.worthy.screen.subscription.domain.model.Subscription
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubscriptionRepositoryImpl(
    private val dao: SubscriptionDao,
    private val cardRepository: CardRepository
): SubscriptionRepository {
    override suspend fun addSubscription(subscription: Subscription) {
        dao.insert(subscription.toEntity())
    }

    override suspend fun updateSubscription(subscription: Subscription) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSubscription(subscriptionId: Int) {
        dao.deleteById(subscriptionId)
    }

    override suspend fun getSubscriptionById(id: Int): Subscription? {
        TODO("Not yet implemented")
    }

    override fun getAllSubscriptions(): Flow<List<Subscription>> {
        TODO("Not yet implemented")
    }

    override fun getSubscriptionsByCardId(cardId: Int): Flow<List<Subscription>> {
        TODO("Not yet implemented")
    }

    override fun getActiveSubscriptions(): Flow<List<Subscription>> {
        // year * 100 + month -> Roomda int olarak bu şekilde saklanıyor
        return dao.getActiveSubscriptions(getCurrentAppDate().toRoomInt()).map { list -> list.map { it.toDomain() } }
    }

    override fun getCards(): Flow<List<Card>> {
        return cardRepository.getAll()
    }
}

