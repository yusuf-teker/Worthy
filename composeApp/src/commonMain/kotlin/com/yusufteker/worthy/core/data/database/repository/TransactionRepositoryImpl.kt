package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.data.database.mappers.toTransactions
import com.yusufteker.worthy.core.data.database.mappers.toTransactionsSince
import com.yusufteker.worthy.core.data.database.model.TransactionDao
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.domain.model.updateAmount
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.core.domain.toEpochMillis
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val cardRepository: CardRepository,
    private val categoryRepository: CategoryRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val currencyConverter: CurrencyConverter,
    private val userPrefsManager: UserPrefsManager

) : TransactionRepository {

    override fun getAll(): Flow<List<Transaction>> {
        return transactionDao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override fun getExpenseCategories(): Flow<List<Category>> {
        return categoryRepository.getByType(CategoryType.EXPENSE)
    }

    override fun getIncomeCategories(): Flow<List<Category>> {
        return categoryRepository.getByType(CategoryType.INCOME)
    }

    override fun getCards(): Flow<List<Card>> {
        return cardRepository.getAll()
    }

    override fun getByCategory(categoryId: Int): Flow<List<Transaction>> {
        return transactionDao.getByCategory(categoryId).map { list -> list.map { it.toDomain() } }
    }

    override fun getByCardId(cardId: Int): Flow<List<Transaction>> {
        return transactionDao.getByCard(cardId).map { list -> list.map { it.toDomain() } }
    }

    override fun getByCardIdConverted(cardId: Int): Flow<List<Transaction>> {
        return transactionDao.getByCard(cardId)
            .map { list ->

                val domainTransactions = list.map { it.toDomain() }

                val selectedCurrency = userPrefsManager.selectedCurrency.first()


                val convertedTransactions = domainTransactions.map { tx ->
                    val convertedAmount = currencyConverter.convert(tx.amount, selectedCurrency)
                    tx.updateAmount(convertedAmount)
                }

                convertedTransactions
            }
    }


    override fun getByType(type: TransactionType): Flow<List<Transaction>> {
        return transactionDao.getByType(type).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getById(id: Int): Transaction? {
        return transactionDao.getById(id)?.toDomain()
    }

    override suspend fun insert(transaction: Transaction): Long {
        val x = transactionDao.insert(transaction.toEntity())
        transactionDao.updateOriginalId(x.toInt()) // taksitli işmler için original id kullanmam gerekti o yüzden ilk kayıtta original idyi setliyorum

        return x
    }

    override suspend fun insertAll(transactions: List<Transaction>) {
        transactionDao.insertAll(transactions.map { it.toEntity() })
    }

    override suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction.toEntity())
    }

    override suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction.toEntity())
    }

    override suspend fun deleteById(transactionId: Int) {
        transactionDao.deleteById(transactionId)
    }

    override suspend fun deleteByOriginalId(originalId: Int) {
        transactionDao.deleteByOriginalId(originalId)
    }

    override suspend fun deleteAll(items: List<Transaction>) {
        transactionDao.deleteAll(items.map { it.toEntity() })
    }

    override fun getTransactionsSince(startDate: LocalDate): Flow<List<Transaction>> {
        val startMillis = startDate.toEpochMillis()

        val transactions = transactionDao.getTransactionsFrom(startMillis)
            .map { list -> list.map { it.toDomain() } }

        // başlangıç tarihinden sonra aktif olan subscriptionları al ve transactiona çevir
        val subscriptions = subscriptionRepository.getSubscriptionsSince(startDate)
            .map { subs -> subs.flatMap { it.toTransactions() } }


        return combine(transactions, subscriptions) { txs, subs ->
            txs + subs
        }
    }

    override fun getRelatedTransactions(relatedTransactionId: Int): Flow<List<Transaction>> {
        return transactionDao.getRelatedTransactions(relatedTransactionId)
            .map { list -> list.map { it.toDomain() } }
    }

}
