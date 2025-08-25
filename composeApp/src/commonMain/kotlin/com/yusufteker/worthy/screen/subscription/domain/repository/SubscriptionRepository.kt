package com.yusufteker.worthy.screen.subscription.domain.repository

import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.screen.subscription.domain.model.Subscription
import com.yusufteker.worthy.screen.subscription.domain.model.SubscriptionCategory
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {

    suspend fun initializeDefaultCategories()
    suspend fun addSubscription(subscription: Subscription)

    suspend fun updateSubscription(subscription: Subscription)

    suspend fun deleteSubscription(subscriptionId: Int)

    suspend fun getSubscriptionById(id: Int): Subscription?

    fun getAllSubscriptions(): Flow<List<Subscription>>

    fun getSubscriptionsByCardId(cardId: Int): Flow<List<Subscription>>

    fun getActiveSubscriptions(): Flow<List<Subscription>>

    fun getCards(): Flow<List<Card>>

    fun getCategories(): Flow<List<SubscriptionCategory>>

    suspend fun addCategory(category: SubscriptionCategory)

}