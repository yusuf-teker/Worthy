package com.yusufteker.worthy.screen.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.getCurrentLocalDateTime
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.domain.model.getLastMonth
import com.yusufteker.worthy.core.domain.model.getLastMonths
import com.yusufteker.worthy.core.domain.model.getLastSixMonths
import com.yusufteker.worthy.core.domain.model.sumConvertedAmount
import com.yusufteker.worthy.core.domain.model.sumWithCurrencyConverted
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.core.presentation.UiEvent.*
import com.yusufteker.worthy.core.presentation.components.adjustValuesForBarChart
import com.yusufteker.worthy.core.presentation.util.hideKeyboard
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRepository
import com.yusufteker.worthy.screen.dashboard.domain.EvaluationResult
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
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

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state

    init {



        observeData()

    }

    fun onAction(action: DashboardAction) = when (action) {

        DashboardAction.EvaluateButtonClicked -> {
            _state.update {
                it.updateBottomSheet { copy(evaluationResult = null) }.copy(
                    isBottomSheetOpen = true
                )
            }
        }
        is DashboardAction.ChartSelected -> {
            _state.update {
                it.copy(
                    selectedChartIndex = if (state.value.selectedChartIndex != action.index) action.index else null,
                    selectedMiniBarsFraction = state.value.miniBarsFractions.get(action.index),// neden 3ün ilk elemeanı 1 diğerleri sıfı
                    selectedMiniBarsMonths = state.value.miniBarsMonths.get(action.index)
                )
            }
        }

        is DashboardAction.CloseBottomSheetClicked -> {
            _state.update { it.copy(isBottomSheetOpen = false) }
        }

        is DashboardAction.CalculateButtonClicked -> {

            viewModelScope.launch {
                _state.update { it.updateBottomSheet { copy(isCalculating = true) } }
                action.money.let {

                    // harcama currencye cevrilmis income
                    val monthlyIncome = currencyConverter.convert(state.value.totalAllIncomeMoney, to = action.money.currency)
                    val monthlyExpense = currencyConverter.convert(state.value.totalAllExpenseMoney, to = action.money.currency)
                    val desireBudget = currencyConverter.convert(state.value.desireBudget, to = action.money.currency)
                    val incomeMinusExpense = monthlyIncome.amount-monthlyExpense.amount
                    val desirePercent = ((action.money.amount / desireBudget.amount) * 100)
                    val  workHours =  (action.money.amount / (monthlyIncome.amount / state.value.monthlyWorkHours)).toFloat()
                    val   remainingDesire =  (state.value.desireBudget.amount - action.money.amount).toInt()
                    val  currencySymbol = state.value.selectedCurrency.symbol
                    val monthlyIncomePercent = ( action.money.amount / monthlyIncome.amount) * 100
                    val incomeMinusExpensePercent = ( action.money.amount / incomeMinusExpense) * 100

                    _state.update {
                        it.updateBottomSheet {
                            copy(
                                evaluationResult = EvaluationResult(
                                    incomePercent = monthlyIncomePercent ,//incomePercent,
                                    desirePercent = desirePercent,
                                    workHours = workHours,
                                    remainingDesire = remainingDesire,
                                    currencySymbol = currencySymbol,
                                    incomeMinusExpensePercent = incomeMinusExpensePercent,
                                ),
                                isCalculating = false
                            )
                        }
                    }
                }

                hideKeyboard()
            }





        }

        is DashboardAction.OnSelectedMonthChanged -> {
            viewModelScope.launch {
                _state.update { currentState ->
                    currentState.copy(
                        selectedMonthYear = action.appDate,
                        incomeChangeRatio = calculateSelectedMonthIncomeChangeRatio(action.appDate),
                        totalAllIncomeMoney = calculateSelectedMonthAllIncome(action.appDate),
                        totalAllExpenseMoney = calculateSelectedMonthAllExpense(action.appDate)
                    )
                }

                calculateBarRatios(
                    state.value.expenseMonthlyAmountList,
                    state.value.incomeMonthlyAmountList,
                    state.value.recurringIncomeMonthlyAmountList,
                    state.value.recurringExpenseMonthlyAmountList,
                    state.value.wishlistMonthlyAmountList,

                )

            }


        }

        is DashboardAction.AddRecurringClicked -> {
            sendUiEventSafe(NavigateTo(Routes.Settings))
        }
        is DashboardAction.AddTransactionClicked -> {
            sendUiEventSafe(NavigateTo(Routes.Settings))
        }
        is DashboardAction.AddWishlistClicked -> {
            sendUiEventSafe(NavigateTo(Routes.WishlistAdd))
        }

        is DashboardAction.PurchaseButtonClicked -> {
            viewModelScope.launch {
                _state.update { it.updateBottomSheet { copy(isPurchasing = true) } }
                dashboardRepository.addPurchase(action.expense)
                _state.update { it.updateBottomSheet { copy(isPurchasing = false) }.copy(
                    isBottomSheetOpen = false
                ) }
            }

        }
    }

    private fun observeData(){

        viewModelScope.launch {
            showLoading()
            _state.update {
                it.copy(
                    // TODO : repository’den gerçek veriyi çek prefden değil
                    userName = "Yusuf",
                    selectedCurrency = userPrefsManager.selectedCurrency.first(),
                    monthlyWorkHours = userPrefsManager.weeklyWorkHours.first() * 4.33f,
                    desireBudget = userPrefsManager.desireBudget.first() ?: emptyMoney( userPrefsManager.selectedCurrency.first()),
                    savingProgress = 0.36f,
                )
            }


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
                    currentDate = getCurrentLocalDateTime()
                ),
                dashboardRepository.getAllWishlistMonthlyAmount(
                    monthCount = 6,
                    currentDate = getCurrentLocalDateTime()
                ),
                dashboardRepository.getExpenseCategories()
            ) { expenses, incomes, dashboardRecurringData, wishlistItems, categories ->
                _state.update { currentState ->
                    currentState.copy(
                        expenseMonthlyAmountList = expenses,
                        incomeMonthlyAmountList = incomes,
                        recurringIncomeMonthlyAmountList = dashboardRecurringData.incomes,
                        recurringExpenseMonthlyAmountList = dashboardRecurringData.expenses,
                        wishlistMonthlyAmountList = wishlistItems,
                    ).updateBottomSheet { copy(categories = categories) }
                }

                calculateBarRatios(
                    expenses,
                    incomes,
                    dashboardRecurringData.incomes,
                    dashboardRecurringData.expenses,
                    wishlistItems
                )

                hideLoading()


            }.launchIn(viewModelScope)

        }
    }


    private suspend fun calculateBarRatios(
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
        ) ?: emptyMoney( state.value.selectedCurrency)
        val totalIncomeMoney =  incomes.sumConvertedAmount(
            state.value.selectedCurrency,
            state.value.selectedMonthYear,
            currencyConverter
        ) ?: emptyMoney(state.value.selectedCurrency)
        val totalRecurringIncomeMoney = recurringIncomes.sumConvertedAmount(
            state.value.selectedCurrency,
            state.value.selectedMonthYear,
            currencyConverter
        ) ?: emptyMoney(state.value.selectedCurrency)
        val totalRecurringExpenseMoney = recurringExpenses.sumConvertedAmount(
            state.value.selectedCurrency,
            state.value.selectedMonthYear,
            currencyConverter
        ) ?: emptyMoney(state.value.selectedCurrency)
        val totalWishlistMoney = wishlistItems.sumConvertedAmount(
            state.value.selectedCurrency,
            state.value.selectedMonthYear,
            currencyConverter
        ) ?: emptyMoney( state.value.selectedCurrency)

        var totalExpense = totalExpenseMoney.amount + totalRecurringExpenseMoney.amount // + totalWishlistMoney.amount // todo wishlist money kaldırılmadı
        val totalIncome = totalIncomeMoney.amount.plus(totalRecurringIncomeMoney.amount)

        if (totalExpense<= 0 ){
            totalExpense = 1.0;
        }
        // Todo ratio şuan expense göre bunu belki böyle yapmayız
        val recurringExpensesRatio = (totalRecurringExpenseMoney.amount / totalExpense) // fixedExpenses
        val wishlistRatio = ( totalWishlistMoney.amount / totalExpense) // Desires
        val remainingRatio = ( totalIncome - totalExpense ) / totalExpense // Remaining
        val expensesRatio = (totalExpenseMoney.amount /  totalExpense) // Expenses

        val normalizedRatios = adjustValuesForBarChart(normalizeRatios(expensesRatio,recurringExpensesRatio,wishlistRatio,remainingRatio))



        val  totalAllIncomeMoney = Money(totalIncome, state.value.selectedCurrency)
        val  totalAllExpenseMoney = Money(totalExpense, state.value.selectedCurrency)

        val last6Months = getLastSixMonths()
        _state.update { currentState ->
            currentState.copy(
                expensesFraction = normalizedRatios.get(0),
                fixedExpenseFraction = normalizedRatios.get(1),
                desiresSpentFraction = normalizedRatios.get(2),
                remainingFraction = normalizedRatios.get(3),
                totalAllIncomeMoney = totalAllIncomeMoney,
                totalAllExpenseMoney = totalAllExpenseMoney,
                miniBarsFractions = listOf(normalizeLast6Month(recurringExpenses ),normalizeLast6Month(wishlistItems),normalizeLast6Month(emptyList()),normalizeLast6Month(expenses )),
                miniBarsMonths =  List(4) { last6Months }// todo napcam bilmiyorum her türlü 6 ayı ver

            )
        }

    }

    suspend fun showLoading(){
        _state.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
    }

    suspend fun hideLoading(){
        _state.update { currentState ->
            currentState.copy(
                isLoading = false
            )
        }
    }
    private suspend fun calculateSelectedMonthRecurringIncome(month: AppDate): Money{
       return  state.value.recurringIncomeMonthlyAmountList.sumConvertedAmount(
            state.value.selectedCurrency,
           month,
            currencyConverter
        ) ?: emptyMoney( state.value.selectedCurrency)
    }

    private suspend fun calculateSelectedMonthIncome(month: AppDate): Money{
        return  state.value.incomeMonthlyAmountList.sumConvertedAmount(
            state.value.selectedCurrency,
            month,
            currencyConverter
        ) ?: emptyMoney( state.value.selectedCurrency)
    }
    private suspend fun calculateSelectedMonthRecurringExpense(month: AppDate): Money{
        return  state.value.recurringExpenseMonthlyAmountList.sumConvertedAmount(
            state.value.selectedCurrency,
            month,
            currencyConverter
        ) ?: emptyMoney( state.value.selectedCurrency)
    }

    private suspend fun calculateSelectedMonthExpense(month: AppDate): Money{
        return  state.value.expenseMonthlyAmountList.sumConvertedAmount(
            state.value.selectedCurrency,
            month,
            currencyConverter
        ) ?: emptyMoney( state.value.selectedCurrency)
    }

    private suspend fun  calculateSelectedMonthAllIncome(month: AppDate): Money{
        return Money(calculateSelectedMonthIncome(month).amount + calculateSelectedMonthRecurringIncome(month).amount, state.value.selectedCurrency)
    }

    private suspend fun calculateSelectedMonthAllExpense(month: AppDate): Money{
        return Money(calculateSelectedMonthExpense(month).amount + calculateSelectedMonthRecurringExpense(month).amount, state.value.selectedCurrency)
    }
    private suspend fun calculateSelectedMonthIncomeChangeRatio(month: AppDate): Double {
        val lastMonthIncome = calculateSelectedMonthRecurringIncome(month.getLastMonth()).amount + calculateSelectedMonthIncome(month.getLastMonth()).amount
        val selectedMonthIncome = (calculateSelectedMonthRecurringIncome(month).amount) + calculateSelectedMonthIncome(month).amount

        return  (selectedMonthIncome - lastMonthIncome) / selectedMonthIncome * 100
    }



    // Son 6 ayın (daha fazla veya azda verilebilir) aylık toplam tutarını 0-1f aralıgına dönüştürüp liste veriyor
    // bar chartlarda yükseklik için 0-1f aralıgında değer lazım
    suspend fun normalizeLast6Month(list : List<DashboardMonthlyAmount>): List<Float?>{
        if (list.isEmpty())
            return listOf(0f,0f,0f,0f,0f,0f)
        val last6Months = getLastSixMonths() // veya LocalDate.now().monthValue

        val monthlyValues = last6Months.map { month ->
            val item = list.find { it.appDate.month == month }
            item?.amount
                ?.sumWithCurrencyConverted(currencyConverter, state.value.selectedCurrency)
                ?.amount ?: 0.0
        }
        return normalizeRatios(monthlyValues) // 0 - 1 ARALIGINDA
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




