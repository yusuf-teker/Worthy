package com.yusufteker.worthy.screen.analytics.data

import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.screen.analytics.domain.AnalyticsRepository
import com.yusufteker.worthy.screen.card.add.domain.CardRepository
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import kotlinx.coroutines.flow.Flow

class AnalyticsRepositoryImpl(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val cardRepository: CardRepository,
    private val userPrefRepository: UserPrefsManager
): AnalyticsRepository {

    override fun getTransactions(): Flow<List<Transaction>> {
        return transactionRepository.getAll()
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