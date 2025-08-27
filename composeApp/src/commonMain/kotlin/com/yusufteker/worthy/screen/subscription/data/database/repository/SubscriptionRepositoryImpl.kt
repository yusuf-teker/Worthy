package com.yusufteker.worthy.screen.subscription.data.database.repository

import com.yusufteker.worthy.core.data.database.entities.defaultCategoryEntities
import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.data.database.model.CategoryDao
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.RecurringItem
import com.yusufteker.worthy.core.domain.model.defaultCategories
import com.yusufteker.worthy.core.domain.model.toRoomInt
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import com.yusufteker.worthy.screen.subscription.data.database.model.SubscriptionDao
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class SubscriptionRepositoryImpl(

    private val dao: SubscriptionDao,
    private val categoryDao: CategoryDao,
    private val cardRepository: CardRepository
): SubscriptionRepository {

    override suspend fun initializeDefaultCategories() {
        if (categoryDao.getCategoryCount() == 0) {
            categoryDao.insertAll(defaultCategoryEntities)
        }
    }
    override suspend fun addSubscription(subscription: RecurringItem.Subscription) {
        dao.insert(subscription.toEntity())
    }

    override suspend fun updateSubscription(subscription: RecurringItem.Subscription) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSubscription(subscriptionId: Int) {
        dao.deleteById(subscriptionId)
    }

    override suspend fun getSubscriptionById(id: Int): RecurringItem.Subscription? {
        return dao.getById(id)?.toDomain()
    }

    override fun getAllSubscriptions(): Flow<List<RecurringItem.Subscription>> {
        return dao.getAll().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getSubscriptionsByCardId(cardId: Int): Flow<List<RecurringItem.Subscription>> {
        TODO("Not yet implemented")
    }

    override fun getActiveSubscriptions(): Flow<List<RecurringItem.Subscription>> {
        // year * 100 + month -> Roomda int olarak bu şekilde saklanıyor
        return dao.getActiveSubscriptions(getCurrentAppDate().toRoomInt()).map { list -> list.map { it.toDomain() } }
    }

    override fun getCards(): Flow<List<Card>> {
        return cardRepository.getAll()
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAll().map { list -> list.filter { it.type == CategoryType.SUBSCRIPTION }.map { it.toDomain() } }
    }
    override suspend fun addCategory(category: Category) {
        categoryDao.insert(category.toEntity())
    }

}

