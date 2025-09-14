package com.yusufteker.worthy.screen.dashboard.domain.helper

import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.getLastSixMonths
import com.yusufteker.worthy.core.domain.model.sumConvertedAmount
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.core.presentation.components.adjustValuesForBarChart
import com.yusufteker.worthy.core.presentation.util.sumWithCurrencyConverted

class DashboardBarChartCalculator(
    private val currencyConverter: CurrencyConverter
) {

    data class Result(
        val expensesFraction: Float,
        val fixedExpenseFraction: Float,
        val desiresSpentFraction: Float,
        val remainingFraction: Float,
        val totalAllIncomeMoney: Money,
        val totalAllExpenseMoney: Money,
        val expensesMoney: Money,
        val fixedExpenseMoney: Money,
        val desiresSpentMoney: Money,
        val remainingMoney: Money,
        val miniBarsFractions: List<List<Float?>>,
        val miniBarsMonths: List<List<Int>>
    )


    suspend fun calculate(
        expenses: List<DashboardMonthlyAmount>,
        incomes: List<DashboardMonthlyAmount>,
        recurringIncomes: List<DashboardMonthlyAmount>,
        recurringExpenses: List<DashboardMonthlyAmount>,
        wishlistItems: List<DashboardMonthlyAmount>,
        selectedCurrency: Currency,
        selectedMonth: AppDate
    ): Result {

        val totalExpenseMoney = expenses.sumConvertedAmount(selectedCurrency, selectedMonth, currencyConverter) ?: Money(
            0.0,
            selectedCurrency
        )
        val totalIncomeMoney = incomes.sumConvertedAmount(selectedCurrency, selectedMonth, currencyConverter) ?: Money(
            0.0,
            selectedCurrency
        )
        val totalRecurringIncomeMoney = recurringIncomes.sumConvertedAmount(selectedCurrency, selectedMonth, currencyConverter) ?: Money(
            0.0,
            selectedCurrency
        )
        val totalRecurringExpenseMoney = recurringExpenses.sumConvertedAmount(selectedCurrency, selectedMonth, currencyConverter) ?: Money(
            0.0,
            selectedCurrency
        )
        val totalWishlistMoney = wishlistItems.sumConvertedAmount(selectedCurrency, selectedMonth, currencyConverter) ?: Money(
            0.0,
            selectedCurrency
        )

        var totalExpense = totalExpenseMoney.amount + totalRecurringExpenseMoney.amount
        val totalIncome = totalIncomeMoney.amount + totalRecurringIncomeMoney.amount
        if (totalExpense <= 0) totalExpense = 1.0
        val remainingMoney = Money(totalIncome - totalExpense, selectedCurrency)

        val expensesRatio = totalExpenseMoney.amount / totalExpense
        val recurringExpensesRatio = totalRecurringExpenseMoney.amount / totalExpense
        val wishlistRatio = totalWishlistMoney.amount / totalExpense
        val remainingRatio = (totalIncome - totalExpense) / totalExpense

        val normalizedRatios = adjustValuesForBarChart(
            normalizeRatios(expensesRatio, recurringExpensesRatio, wishlistRatio, remainingRatio)
        )


        val last6Months = getLastSixMonths()


        return Result(
            expensesFraction = normalizedRatios[0],
            fixedExpenseFraction = normalizedRatios[1],
            desiresSpentFraction = normalizedRatios[2],
            remainingFraction = normalizedRatios[3],
            totalAllIncomeMoney = Money(totalIncome, selectedCurrency),
            totalAllExpenseMoney = Money(totalExpense, selectedCurrency),
            miniBarsFractions = listOf(
                normalizeLast6Month(recurringExpenses),
                normalizeLast6Month(wishlistItems),
                normalizeLast6Month(emptyList()),
                normalizeLast6Month(expenses)
            ),
            miniBarsMonths = List(4) { last6Months },
            expensesMoney = totalExpenseMoney,
            fixedExpenseMoney = totalRecurringExpenseMoney,
            desiresSpentMoney = totalWishlistMoney,
            remainingMoney = remainingMoney

        )
    }

    private suspend fun normalizeLast6Month(list: List<DashboardMonthlyAmount>): List<Float?> {
        if (list.isEmpty()) return listOf(0f, 0f, 0f, 0f, 0f, 0f)
        val last6Months = getLastSixMonths()
        val monthlyValues = last6Months.map { month ->
            val item = list.find { it.appDate.month == month }
            item?.amount?.sumWithCurrencyConverted(currencyConverter, list.firstOrNull()?.amount?.firstOrNull()?.currency ?: Currency.USD)?.amount ?: 0.0
        }
        return normalizeRatios(monthlyValues)
    }

    private fun normalizeRatios(ratios: List<Double>): List<Float?> {
        if (ratios.isEmpty()) return emptyList()
        val max = ratios.maxOrNull() ?: 1.0
        return ratios.map { if (it > 0) (it / max).toFloat() else null }
    }

    private fun normalizeRatios(vararg ratios: Double): List<Double?> {
        val max = ratios.maxOrNull() ?: 1.0
        return ratios.map { if (it > 0.0) it / max else null }
    }
}