package com.yusufteker.worthy.screen.transactions.data.repository

import com.yusufteker.worthy.core.data.database.mappers.toTransactions
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.screen.transactions.domain.repository.AnalyticsRepository
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class AnalyticsRepositoryImpl(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val cardRepository: CardRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val userPrefRepository: UserPrefsManager
): AnalyticsRepository {

    override fun getTransactions(): Flow<List<Transaction>> {
        return combine(
            subscriptionRepository.getAllSubscriptions(), // Flow<List<Subscription>>
            transactionRepository.getAll()                // Flow<List<Transaction>>
        ) { subscriptions, transactions ->
            // Subscriptionları transaction listesine çevir
            val subscriptionTransactions: List<Transaction> = subscriptions.flatMap { it.toTransactions() }

            // Mevcut transactionlar ile birleştir
            transactions + subscriptionTransactions
        }
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryRepository.getAll()
    }

    override fun getCards(): Flow<List<Card>> {
        return cardRepository.getAll()
    }

    override suspend fun deleteTransaction(id: Int) {
        transactionRepository.deleteById(id)
    }

    override fun getUserPrefCurrency(): Flow<Currency> {
        return userPrefRepository.selectedCurrency
    }




}