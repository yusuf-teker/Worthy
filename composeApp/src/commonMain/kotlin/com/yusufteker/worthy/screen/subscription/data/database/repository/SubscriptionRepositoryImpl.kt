package com.yusufteker.worthy.screen.subscription.data.database.repository

import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.model.toRoomInt
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import com.yusufteker.worthy.screen.subscription.data.database.entities.defaultSubscriptionCategoriesEntities
import com.yusufteker.worthy.screen.subscription.data.database.model.SubscriptionCategoryDao
import com.yusufteker.worthy.screen.subscription.data.database.model.SubscriptionDao
import com.yusufteker.worthy.screen.subscription.domain.model.Subscription
import com.yusufteker.worthy.screen.subscription.domain.model.SubscriptionCategory
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubscriptionRepositoryImpl(

    private val dao: SubscriptionDao,
    private val subscriptionCategoryDao: SubscriptionCategoryDao,
    private val cardRepository: CardRepository
): SubscriptionRepository {

    override suspend fun initializeDefaultCategories() {
        if (subscriptionCategoryDao.getCategoryCount() == 0) {
            subscriptionCategoryDao.insertAll(defaultSubscriptionCategoriesEntities)
        }
    }
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

    override fun getCategories(): Flow<List<SubscriptionCategory>> {
        return subscriptionCategoryDao.getAll().map { list -> list.map { it.toDomain() } }
    }
    override suspend fun addCategory(category: SubscriptionCategory) {
        subscriptionCategoryDao.insert(category.toEntity())
    }

}

