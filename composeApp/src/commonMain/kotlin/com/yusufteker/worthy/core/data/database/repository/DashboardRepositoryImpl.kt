package com.yusufteker.worthy.core.data.database.repository


import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.YearMonth
import com.yusufteker.worthy.core.domain.model.generateMonthlyAmounts
import com.yusufteker.worthy.core.domain.repository.*
import com.yusufteker.worthy.core.domain.toEpochMillis
import com.yusufteker.worthy.core.domain.toLocalDate
import com.yusufteker.worthy.screen.dashboard.domain.*
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number


class DashboardRepositoryImpl(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val recurringRepository: RecurringFinancialItemRepository,
    private val wishlistRepository: WishlistRepository,
    private val categoryRepository: CategoryRepository
) : DashboardRepository {

    override fun getAllRecurringMonthlyAmount(
        monthCount: Int,
        currentDate: LocalDate
    ): Flow<DashboardRecurringData>{
        return recurringRepository.getAll()
            .map { items ->
                val incomes = items.filter { it.isIncome }
                val expenses = items.filter { !it.isIncome }
                val incomeAmounts = generateMonthlyAmounts(
                    items = incomes,
                    referenceDate = currentDate,
                    monthCount = monthCount
                )
                val expenseAmounts = generateMonthlyAmounts(
                    items = expenses,
                    referenceDate = currentDate,
                    monthCount = monthCount
                )
                DashboardRecurringData(
                    incomes = incomeAmounts,
                    expenses = expenseAmounts
                )
            }

    }

    override fun getAllIncomeMonthlyAmount(
        monthCount: Int,
        currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>> {
        val startDate = currentDate
            .minus(monthCount.toLong(), DateTimeUnit.MONTH)

        val firstDayOfStartMonth = LocalDate(startDate.year, startDate.month.number, 1)
        return incomeRepository.getIncomesSince(firstDayOfStartMonth)
            .map { incomes ->
                val grouped: Map<YearMonth, List<Income>> = incomes.groupBy { income ->
                    YearMonth(year = income.date.toLocalDate().year,  month= income.date.toLocalDate().month.number)
                }
                grouped.map { (yearMonth, incomeList) ->
                    DashboardMonthlyAmount(
                        yearMonth = yearMonth,
                        amount = incomeList.map { it.amount }
                    )
                }.sortedWith(compareBy({ it.yearMonth.year }, { it.yearMonth.month }))
            }
    }

    override fun getAllExpenseMonthlyAmount(
        monthCount: Int,
        currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>> {
        val startDate = currentDate
            .minus(monthCount.toLong(), DateTimeUnit.MONTH)

        val firstDayOfStartMonth = LocalDate(startDate.year, startDate.month.number, 1)
        return expenseRepository.getExpensesSince(firstDayOfStartMonth)
            .map { expenses ->
                val grouped: Map<YearMonth, List<Expense>> = expenses.groupBy { expense ->
                    YearMonth(year = expense.date.toLocalDate().year,  month= expense.date.toLocalDate().month.number)
                }
                grouped.map { (yearMonth, expenseList) ->
                    DashboardMonthlyAmount(
                        yearMonth = yearMonth,
                        amount = expenseList.map { it.amount }
                    )
                }.sortedWith(compareBy({ it.yearMonth.year }, { it.yearMonth.month }))
            }
    }

    override fun getAllWishlistMonthlyAmount(
        monthCount: Int,
        currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>> {

        return wishlistRepository.getAll() // todo şuan alınmayanları gösteriyor burada logici o ay içinde alınanlar ile değiştiricem
            .map { wishlistItems ->
                val unpurchased = wishlistItems.filter { !it.isPurchased }

                val monthSet = (0 until monthCount).map { offset ->
                    val month = currentDate.minus(offset.toLong(), DateTimeUnit.MONTH)
                    LocalDate(month.year, month.monthNumber, 1)
                }

                val result = mutableMapOf<Pair<Int, Int>, MutableList<Money>>()

                for (monthDate in monthSet) {
                    val yearMonth = monthDate.year to monthDate.monthNumber

                    for (item in unpurchased) {
                        if (item.addedDate <= monthDate.toEpochMillis()) {
                            result.getOrPut(yearMonth) { mutableListOf() }.add(item.price)
                        }
                    }
                }

                result.map { (yearMonth, amounts) ->
                    DashboardMonthlyAmount(
                        YearMonth(yearMonth.first,  yearMonth.second),
                        amount = amounts
                    )
                }.sortedWith(compareBy({ it.yearMonth.year }, { it.yearMonth.month }))
            }
    }

    override fun getExpenseCategories(): Flow<List<Category>> {
        return categoryRepository.getByType(CategoryType.EXPENSE)
    }


}
