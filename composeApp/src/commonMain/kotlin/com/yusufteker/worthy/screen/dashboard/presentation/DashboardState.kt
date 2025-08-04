package com.yusufteker.worthy.screen.dashboard.presentation

import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.getCurrentYearMonth
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.MonthlyAmount
import com.yusufteker.worthy.core.domain.model.YearMonth
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRecurringData
import com.yusufteker.worthy.screen.dashboard.domain.EvaluationResult

data class DashboardState(
    val userName: String = "",
    val selectedCurrency: Currency = Currency.TRY,
    val monthlyWorkHours: Float = 0f,
    val desireBudget: Double = 0.0,
    val savingProgress: Float = 0f,   // 0f – 1f
    val isLoading: Boolean = false,




    var selectedChartIndex: Int? = null,
    var isBottomSheetOpen: Boolean = false,
    var bottomSheetResult: String? = null,
    var evaluationResult: EvaluationResult? = null,
    var categories: List<Category> = emptyList(),



    val fixedExpenseFraction: Float = 0f,
    val desiresSpentFraction: Float = 0f,
    val remainingFraction: Float = 0f,
    val expensesFraction: Float = 0f,

    val miniBarsFractions: List<List<Float?>> = emptyList(),
    val selectedMiniBarsFraction: List<Float?> = emptyList(),
    val miniBarsMonths:List<List<Int>> = emptyList(),
    val selectedMiniBarsMonths: List<Int> = emptyList(),



    val totalCurrentIncomeRecurringMoney: Money = Money(0.0, selectedCurrency),

    val dashboardRecurringData: DashboardRecurringData? = null,
    val recurringIncomeMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),
    val filteredRecurringIncomeMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),
    val recurringExpenseMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),
    val filteredRecurringExpenseMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),

    val expenseMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),
    val incomeMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),
    val filteredExpenseMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),
    val filteredIncomeMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),

    val wishlistMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),
    val filteredWishlistMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),

    val selectedMonthYear: YearMonth = getCurrentYearMonth(),
    val selectableMonths: List<YearMonth> = getRecentYearMonths(currentMonth = getCurrentMonth(), currentYear = getCurrentYear()),

    )

fun getSelectableMonthIndices(currentMonth: Int, count: Int = 6): List<Int> {
    return List(count + 1) { i ->
        val month = currentMonth - i
        if (month > 0) month else 12 + month
    }
}
fun getRecentYearMonths(
    currentYear: Int,
    currentMonth: Int,
    count: Int = 6
): List<YearMonth> {
    val result = mutableListOf<YearMonth>()
    var year = currentYear
    var month = currentMonth

    repeat(count + 1) {
        result.add(YearMonth(year = year, month = month))
        month -= 1
        if (month == 0) {
            month = 12
            year -= 1
        }
    }


    return result

}
