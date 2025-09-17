package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.generateMonthlyAmounts
import com.yusufteker.worthy.core.domain.model.groupByMonthForDashboard
import com.yusufteker.worthy.core.domain.model.groupByMonthForDashboardIncome
import com.yusufteker.worthy.core.domain.model.splitInstallmentsByFirstPaymentDate
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.RecurringFinancialItemRepository
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.domain.toEpochMillis
import com.yusufteker.worthy.core.domain.toLocalDate
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRecurringData
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRepository
import com.yusufteker.worthy.screen.dashboard.domain.helper.calculateWishlistAmounts
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number

class DashboardRepositoryImpl(
    private val recurringRepository: RecurringFinancialItemRepository,
    private val wishlistRepository: WishlistRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,

    ) : DashboardRepository {

    override fun getAllRecurringMonthlyAmount(
        monthCount: Int, currentDate: LocalDate
    ): Flow<DashboardRecurringData> {
        return recurringRepository.getAll().map { items ->
            val (incomes, expenses) = items.partition { it.isIncome }

            val incomeAmounts = generateMonthlyAmounts(
                items = incomes, referenceDate = currentDate, monthCount = monthCount
            )
            val expenseAmounts = generateMonthlyAmounts(
                items = expenses, referenceDate = currentDate, monthCount = monthCount
            )

            DashboardRecurringData(
                incomes = incomeAmounts, expenses = expenseAmounts
            )
        }
    }

    private fun getStartDate(monthCount: Int, currentDate: LocalDate): LocalDate {
        val startDate = currentDate.minus(monthCount.toLong(), DateTimeUnit.MONTH)
        return LocalDate(startDate.year, startDate.month.number, 1)
    }

    // BURADA SPLIT ETMIYORUM TAKSITLI GELIR YOK
    override fun getAllIncomeMonthlyAmount(
        monthCount: Int, currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>> {
        val firstDayOfStartMonth = getStartDate(monthCount, currentDate)

        return transactionRepository.getIncomeTransactionsSince(
            firstDayOfStartMonth
        ).map { transactions ->
            transactions.groupByMonthForDashboardIncome()
        }
    }

    override fun getAllExpenseMonthlyAmount(
        monthCount: Int, currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>> {

        val firstDayOfStartMonth = getStartDate(monthCount, currentDate)

        return combine(
            transactionRepository.getTransactionsSince(
                firstDayOfStartMonth, TransactionType.EXPENSE
            ), transactionRepository.getCards()
        ) { transactions, cards ->

            // TAKSITLI ISE HER BIR TAKSITI AY AY AYIR
            val cardMap = cards.associateBy { it.id }  // cardId - Card

            transactions.flatMap { tx -> tx.splitInstallmentsByFirstPaymentDate(cardMap[tx.cardId]) }
                .groupByMonthForDashboard()

        }
    }

    // Satın alınmayan wishlist seçili ay veya daha öncesinde eklenmiş ise döndürür
    // 6.ayda eklenmişse 6 7 8 9 10 11 12. aylarda gözükür
    override fun getAllWishlistMonthlyAmount(
        monthCount: Int, currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>> {

        return wishlistRepository.getAllUnpurchased().map { wishlistItems ->

            if (wishlistItems.isEmpty()) {
                return@map emptyList<DashboardMonthlyAmount>()
            }

            val sortedItems = wishlistItems.sortedBy { it.addedDate.toLocalDate() }

            val monthDates = (0 until monthCount).asSequence().map { offset ->
                val month = currentDate.minus(offset.toLong(), DateTimeUnit.MONTH)
                LocalDate(month.year, month.month.number, 1)
            }.sortedBy { it.toEpochMillis() }.toList()

            calculateWishlistAmounts(sortedItems, monthDates)
        }
    }

    override fun getExpenseCategories(): Flow<List<Category>> {
        return categoryRepository.getByType(CategoryType.EXPENSE)
    }

    override suspend fun addPurchase(
        expense: Transaction
    ) {
        transactionRepository.insert(expense)
    }

}
