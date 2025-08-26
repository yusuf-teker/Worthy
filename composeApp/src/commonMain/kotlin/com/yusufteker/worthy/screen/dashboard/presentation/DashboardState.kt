package com.yusufteker.worthy.screen.dashboard.presentation

import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.currentAppDate
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.base.BaseState
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRecurringData
import com.yusufteker.worthy.screen.dashboard.presentation.components.BottomSheetUiState

data class DashboardState(
    val userName: String = "",
    val selectedCurrency: Currency = Currency.TRY,
    val monthlyWorkHours: Float = 0f,
    val desireBudget: Money = emptyMoney(selectedCurrency),
    val savingProgress: Float = 0f,   // 0f – 1f

    var selectedChartIndex: Int? = null,

    //Bottom Sheet
    var isBottomSheetOpen: Boolean = false,
    var bottomSheetUiState: BottomSheetUiState = BottomSheetUiState(),

    val fixedExpenseFraction: Float = 0f,
    val fixedExpenseMoney: Money = emptyMoney(selectedCurrency),
    val desiresSpentFraction: Float = 0f,
    val desiresSpentMoney: Money = emptyMoney(selectedCurrency),
    val remainingFraction: Float = 0f,
    val remainingMoney: Money = emptyMoney(selectedCurrency),
    val expensesFraction: Float = 0f,
    val expensesMoney: Money = emptyMoney(selectedCurrency),

    // Mini Bar
    val miniBarsFractions: List<List<Float?>> = emptyList(),
    val selectedMiniBarsFraction: List<Float?> = emptyList(),
    val miniBarsMonths: List<List<Int>> = emptyList(),
    val selectedMiniBarsMonths: List<Int> = emptyList(),

    //Total Income
    val totalSelectedMonthIncomeRecurringMoney: Money = emptyMoney(selectedCurrency),
    val totalSelectedMonthIncomeMoney: Money = emptyMoney(selectedCurrency),
    val totalAllIncomeMoney: Money = emptyMoney(selectedCurrency),
    val totalAllExpenseMoney: Money = emptyMoney(selectedCurrency),

    val incomeChangeRatio: Double = 0.0,

    //BAR CHART
    val dashboardRecurringData: DashboardRecurringData? = null,

    val recurringIncomeMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),
    val recurringExpenseMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),

    val expenseMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),
    val incomeMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),

    val wishlistMonthlyAmountList: List<DashboardMonthlyAmount> = emptyList(),

    val selectedMonthYear: AppDate = currentAppDate(),
    val selectableMonths: List<AppDate> = getRecentYearMonths(
        currentMonth = getCurrentMonth(),
        currentYear = getCurrentYear()
    ),
    override val isLoading: Boolean = false
) : BaseState {
    override fun copyWithLoading(isLoading: Boolean) = copy(isLoading = isLoading)

}

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
): List<AppDate> {
    val result = mutableListOf<AppDate>()
    var year = currentYear
    var month = currentMonth

    repeat(count + 1) {
        result.add(AppDate(year = year, month = month))
        month -= 1
        if (month == 0) {
            month = 12
            year -= 1
        }
    }


    return result

}

fun DashboardState.updateBottomSheet(update: BottomSheetUiState.() -> BottomSheetUiState): DashboardState {
    return copy(bottomSheetUiState = bottomSheetUiState.update())
}
