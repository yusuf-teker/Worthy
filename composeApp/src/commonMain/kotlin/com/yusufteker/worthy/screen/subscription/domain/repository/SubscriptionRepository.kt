package com.yusufteker.worthy.screen.subscription.domain.repository

import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.RecurringItem
import com.yusufteker.worthy.screen.card.domain.model.Card

import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {

    suspend fun initializeDefaultCategories()
    suspend fun addSubscription(subscription: RecurringItem.Subscription)

    suspend fun updateSubscription(subscription: RecurringItem.Subscription)

    suspend fun deleteSubscription(subscriptionId: Int)

    suspend fun getSubscriptionById(id: Int): RecurringItem.Subscription?

    fun getAllSubscriptions(): Flow<List<RecurringItem.Subscription>>

    fun getSubscriptionsByCardId(cardId: Int): Flow<List<RecurringItem.Subscription>>

    fun getActiveSubscriptions(): Flow<List<RecurringItem.Subscription>>

    fun getCards(): Flow<List<Card>>

    fun getCategories(): Flow<List<Category>>

    suspend fun addCategory(category: Category)

}