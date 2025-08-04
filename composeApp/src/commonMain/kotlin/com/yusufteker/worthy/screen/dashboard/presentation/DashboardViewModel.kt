package com.yusufteker.worthy.screen.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.getCurrentLocalDateTime
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.MonthlyAmount
import com.yusufteker.worthy.core.domain.model.YearMonth
import com.yusufteker.worthy.core.domain.model.getCurrentYearMonth
import com.yusufteker.worthy.core.domain.model.getLastMonths
import com.yusufteker.worthy.core.domain.model.sumConvertedAmount
import com.yusufteker.worthy.core.domain.model.sumWithCurrencyConverted
import com.yusufteker.worthy.core.domain.model.sumWithoutCurrencyConverted
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRepository
import com.yusufteker.worthy.screen.dashboard.domain.EvaluationResult
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userPrefsManager: UserPrefsManager,
    private val dashboardRepository: DashboardRepository,
    private val currencyConverter: CurrencyConverter
) : BaseViewModel() {

    private val _state = MutableStateFlow(DashboardState(isLoading = true,))
    val state: StateFlow<DashboardState> = _state

    init {

        observeData()

        fetchDashboardData()
    }

    fun onAction(action: DashboardAction) = when (action) {
        DashboardAction.Refresh -> fetchDashboardData()
        DashboardAction.EvaluateButtonClicked -> {
            _state.update { it.copy(isBottomSheetOpen = true) }
        }
        is DashboardAction.ChartSelected -> {
            _state.update {
                it.copy(
                    selectedChartIndex = if (state.value.selectedChartIndex != action.index) action.index else null,
                    selectedMiniBarsFraction = state.value.miniBarsFractions.get(action.index),
                    selectedMiniBarsMonths = state.value.miniBarsMonths.get(action.index)
                )
            }
        }

        DashboardAction.CloseBottomSheetClicked -> {
            _state.update { it.copy(isBottomSheetOpen = false) }
        }

        is DashboardAction.CalculateButtonClicked -> {

            viewModelScope.launch {
                action.money.let {

                    // harcama currencye cevrilmis income
                    val monthlyIncome = currencyConverter.convert(state.value.totalCurrentIncomeRecurringMoney, to = action.money.currency)

                    val desirePercent = ((action.money.amount / state.value.desireBudget)*100).toDouble()
                    val  workHours =  (action.money.amount / (monthlyIncome.amount / state.value.monthlyWorkHours)).toFloat()
                    val   remainingDesire =  (state.value.desireBudget - action.money.amount).toInt()
                    val  currencySymbol = state.value.selectedCurrency.symbol
                    _state.update {
                        it.copy(
                            evaluationResult = EvaluationResult(
                                incomePercent = 0.0,//incomePercent,
                                desirePercent = desirePercent,
                                workHours = workHours,
                                remainingDesire = remainingDesire,
                                currencySymbol = currencySymbol,
                            ),
                        )
                    }
                }
            }





        }

        is DashboardAction.OnSelectedMonthChanged -> {
            _state.update { currentState ->
                currentState.copy(
                    selectedMonthYear = action.yearMonth,
                )
            }

            filteredMonthlyAmounts(action.yearMonth)
        }
    }

    private fun filteredMonthlyAmounts(yearMonth: YearMonth) {

        val filteredIncomeList = state.value.recurringIncomeMonthlyAmountList.filter {
            it.yearMonth == yearMonth
        }

        val filteredExpenseList = state.value.recurringExpenseMonthlyAmountList.filter {
            it.yearMonth == yearMonth
        }

        _state.update { currentState ->
            currentState.copy(
                filteredRecurringIncomeMonthlyAmountList = filteredIncomeList,
                filteredRecurringExpenseMonthlyAmountList = filteredExpenseList
            )
        }
    }

    private fun fetchDashboardData() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        // TODO: repository’den gerçek veriyi çek
        delay(800)    // demo loading

        _state.update {
            it.copy( // TODO : repository’den gerçek veriyi çek prefden değil
                userName = "Yusuf",
                //monthlyIncome = userPrefsManager.totalIncome.first().toDouble(),
                selectedCurrency = userPrefsManager.selectedCurrency.first(),
                monthlyWorkHours = userPrefsManager.weeklyWorkHours.first() * 4.33f,
                desireBudget = 10000.0,
               // monthlyExpense = 27000.0,
                savingProgress = 0.36f,
                isLoading = false,
            )
        }
    }





    private fun observeData(){
        combine(
            dashboardRepository.getAllExpenseMonthlyAmount(
                6,
                getCurrentLocalDateTime()
            ),
            dashboardRepository.getAllIncomeMonthlyAmount(
                6,
                getCurrentLocalDateTime()
            ),
            dashboardRepository.getAllRecurringMonthlyAmount(
                monthCount = 6,
                currentDate =  getCurrentLocalDateTime()
            ),
            dashboardRepository.getAllWishlistMonthlyAmount(
                monthCount = 6,
                currentDate = getCurrentLocalDateTime()
            )
        ) {  expenses, incomes, dashboardRecurringData, wishlistItems  ->
            _state.update { currentState ->
                currentState.copy(
                    expenseMonthlyAmountList = expenses,
                    incomeMonthlyAmountList = incomes,
                    recurringIncomeMonthlyAmountList = dashboardRecurringData.incomes,
                    recurringExpenseMonthlyAmountList = dashboardRecurringData.expenses,
                    wishlistMonthlyAmountList = wishlistItems,
                )
            }

            calculate(
                expenses,
                incomes,
                dashboardRecurringData.incomes,
                dashboardRecurringData.expenses,
                wishlistItems
            )

        }.launchIn(viewModelScope)

    }


    private suspend fun calculate(
        expenses: List<DashboardMonthlyAmount>, // LAST 6 MONTHS DATA
        incomes: List<DashboardMonthlyAmount>,
        recurringIncomes: List<DashboardMonthlyAmount>,
        recurringExpenses: List<DashboardMonthlyAmount>,
        wishlistItems: List<DashboardMonthlyAmount>
    ) {
       val totalExpenseMoney=  expenses.sumConvertedAmount( // Seçili ayın Toplam Gideri
            state.value.selectedCurrency,
            state.value.selectedMonthYear,
            currencyConverter
        ) ?: Money(0.0, state.value.selectedCurrency)
        val totalIncomeMoney =  incomes.sumConvertedAmount(
            state.value.selectedCurrency,
            state.value.selectedMonthYear,
            currencyConverter
        ) ?: Money(0.0, state.value.selectedCurrency)
        val totalRecurringIncomeMoney = recurringIncomes.sumConvertedAmount(
            state.value.selectedCurrency,
            state.value.selectedMonthYear,
            currencyConverter
        ) ?: Money(0.0, state.value.selectedCurrency)
        val totalRecurringExpenseMoney = recurringExpenses.sumConvertedAmount(
            state.value.selectedCurrency,
            state.value.selectedMonthYear,
            currencyConverter
        ) ?: Money(0.0, state.value.selectedCurrency)
        val totalWishlistMoney = wishlistItems.sumConvertedAmount(
            state.value.selectedCurrency,
            state.value.selectedMonthYear,
            currencyConverter
        ) ?: Money(0.0, state.value.selectedCurrency)

        val totalIncome = totalIncomeMoney.amount.plus(totalRecurringIncomeMoney.amount)
        val totalExpense = totalExpenseMoney.amount + totalRecurringExpenseMoney.amount + totalWishlistMoney.amount

        // Todo ratio şuan expense göre bunu belki böyle yapmayız
        val expensesRatio = (totalExpenseMoney.amount /  totalExpense)
        val recurringExpensesRatio = (totalRecurringExpenseMoney.amount / totalExpense)
        val wishlistRatio = ( totalWishlistMoney.amount / totalExpense)
        val remainingRatio = ( totalIncome - totalExpense ) / totalExpense

        val normalizedRatios = normalizeRatios(expensesRatio,recurringExpensesRatio,wishlistRatio,remainingRatio)

        // Total Current RecurringMonet
        val totalCurrentRecurringMoney =  recurringIncomes.sumConvertedAmount(
        state.value.selectedCurrency,
            getCurrentYearMonth(),
        currencyConverter
        ) ?: Money(0.0, state.value.selectedCurrency)



        _state.update { currentState ->
            currentState.copy(
                expensesFraction = normalizedRatios.get(0)?.toFloat() ?: -1f,
                desiresSpentFraction = normalizedRatios.get(2)?.toFloat() ?: -1f,
                fixedExpenseFraction = normalizedRatios.get(1)?.toFloat() ?: -1f,
                remainingFraction = normalizedRatios.get(3)?.toFloat() ?: -1f,
                totalCurrentIncomeRecurringMoney = totalCurrentRecurringMoney,
                //todo  expense ile income arasındaki farkları bakan fonk yazılcak
                miniBarsFractions = listOf(normalizeLast6Month(recurringExpenses ),normalizeLast6Month(wishlistItems),  normalizeLast6Month(expenses ),normalizeLast6Month(emptyList())),
                miniBarsMonths =  listOf(recurringExpenses.getLastMonths(6),wishlistItems.getLastMonths(6),expenses.getLastMonths(6),expenses.getLastMonths(6)) // TODO EXPENSE ile aynı yapıldı

            )
        }


        // todo oranları hesaplayı ona göre ekranda yükseklik belirke

    }

    // Son 6 ayın (daha fazla veya azda verilebilir) aylık toplam tutarını 0-1f aralıgına dönüştürüp liste veriyor
    // bar chartlarda yükseklik için 0-1f aralıgında değer lazım
    suspend fun normalizeLast6Month(list : List<DashboardMonthlyAmount>): List<Float?>{
        if (list.isEmpty())
            return emptyList()
        val last6Month = list.map { dashboardMonthlyAmount ->
            // Total money for each month with selected currency
            dashboardMonthlyAmount.amount.sumWithCurrencyConverted(currencyConverter, state.value.selectedCurrency).amount
        }
        return normalizeRatios(last6Month) // 0 - 1 ARALIGINDA
    }

}

// 0 ile 1 arasında değerler verir
fun normalizeRatios(vararg ratios: Double): List<Double?> {
    val max = ratios.max()
    val validRatios = ratios.map { it.coerceAtLeast(0.0) }
    val total = validRatios.sum()

    return if (total == 0.0) {
        List(ratios.size) { 0.0 }
    } else {
        ratios.map {
            if (it > 0.0 ){
                it / max
            }else{
                null
            }
        }
    }
}

fun normalizeRatios( ratios: List<Double>): List<Float?> {
    if (ratios.isEmpty())
        return emptyList()

    val max = ratios.max()
    val validRatios = ratios.map { it.coerceAtLeast(0.0) }
    val total = validRatios.sum()

    return if (total == 0.0) {
        List(ratios.size) { 0f }
    } else {
        ratios.map {
            if (it > 0.0 ){
                (it / max).toFloat()
            }else{
                null
            }
        }
    }
}




