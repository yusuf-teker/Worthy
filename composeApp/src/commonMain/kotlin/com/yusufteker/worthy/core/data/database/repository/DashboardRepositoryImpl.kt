package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.generateMonthlyAmounts
import com.yusufteker.worthy.core.domain.model.splitInstallments
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.RecurringFinancialItemRepository
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.domain.toEpochMillis
import com.yusufteker.worthy.core.domain.toLocalDate
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRecurringData
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRepository
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
            val incomes = items.filter { it.isIncome }
            val expenses = items.filter { !it.isIncome }
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

    // BURADA SPLIT ETMIYORUM TAKSITLI GELIR YOK
    override fun getAllIncomeMonthlyAmount(
        monthCount: Int, currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>> {
        val startDate = currentDate.minus(monthCount.toLong(), DateTimeUnit.MONTH)

        val firstDayOfStartMonth = LocalDate(startDate.year, startDate.month.number, 1)

        return transactionRepository.getTransactionsSince(firstDayOfStartMonth)
            .map { transactions ->
                val incomes = transactions.filter { it.transactionType == TransactionType.INCOME }

                val grouped: Map<AppDate, List<Transaction>> = incomes.groupBy { tx ->
                    val localDate = tx.transactionDate.toLocalDate()
                    AppDate(year = localDate.year, month = localDate.month.number)
                }

                grouped.map { (yearMonth, incomeList) ->
                    DashboardMonthlyAmount(
                        appDate = yearMonth, amount = incomeList.map { it.amount })
                }.sortedWith(compareBy({ it.appDate.year }, { it.appDate.month }))
            }
    }

    override fun getAllExpenseMonthlyAmount(
        monthCount: Int, currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>> {
        val startDate = currentDate.minus(monthCount.toLong(), DateTimeUnit.MONTH)
        val firstDayOfStartMonth = LocalDate(startDate.year, startDate.month.number, 1)

        return combine(
            transactionRepository.getTransactionsSince(firstDayOfStartMonth),
                    transactionRepository.getCards()
        ) { transactions, cards ->

            // TAKSITLI ISE HER BIR TAKSITI AY AY AYIR
            val splitTransactions = transactions.flatMap { tx ->
                val card = cards.find { it.id == tx.cardId } // cardId eşleşmesi
                tx.splitInstallments(card)
            }

            val expenses = splitTransactions.filter { it.transactionType == TransactionType.EXPENSE }

            val grouped: Map<AppDate, List<Transaction>> = expenses.groupBy { tx ->
                val localDate = tx.transactionDate.toLocalDate()
                AppDate(year = localDate.year, month = localDate.month.number)
            }

            grouped.map { (yearMonth, expenseList) ->
                DashboardMonthlyAmount(
                    appDate = yearMonth,
                    amount = expenseList.map { it.amount }
                )
            }.sortedWith(compareBy({ it.appDate.year }, { it.appDate.month }))
        }
    }


    // Satın alınmayan wishlist seçili ay veya daha öncesinde eklenmiş ise döndürür
    // 6.ayda eklenmişse 6 7 8 9 10 11 12. aylarda gözükür
    override fun getAllWishlistMonthlyAmount(
        monthCount: Int, currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>> {

        return wishlistRepository.getAll() // todo şuan alınmayanları gösteriyor burada logici o ay içinde alınanlar ile değiştiricem
            .map { wishlistItems ->
                val unpurchased = wishlistItems.filter { !it.isPurchased }

                val monthSet = (0 until monthCount).map { offset ->
                    val month = currentDate.minus(offset.toLong(), DateTimeUnit.MONTH)
                    LocalDate(month.year, month.month.number, 1)
                }

                val result = mutableMapOf<Pair<Int, Int>, MutableList<Money>>()

                for (monthDate in monthSet) {
                    val yearMonth = monthDate.year to monthDate.month.number

                    for (item in unpurchased) {
                        val itemFirstDayOfMonth = item.addedDate.toLocalDate().let { LocalDate(it.year, it.month.number, 1) }

                        if (itemFirstDayOfMonth <= monthDate) {
                            result.getOrPut(yearMonth) { mutableListOf() }.add(item.price)
                        }
                    }
                }

                result.map { (yearMonth, amounts) ->
                    DashboardMonthlyAmount(
                        AppDate(yearMonth.first, yearMonth.second), amount = amounts
                    )
                }.sortedWith(compareBy({ it.appDate.year }, { it.appDate.month }))
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
