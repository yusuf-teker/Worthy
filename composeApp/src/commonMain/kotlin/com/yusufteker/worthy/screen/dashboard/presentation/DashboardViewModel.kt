package com.yusufteker.worthy.screen.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.getCurrentLocalDateTime
import com.yusufteker.worthy.core.domain.model.MonthlyAmount
import com.yusufteker.worthy.core.domain.model.YearMonth
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.core.presentation.theme.Constants.currencySymbols
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRepository
import com.yusufteker.worthy.screen.dashboard.domain.EvaluationResult
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class DashboardViewModel(
    private val userPrefsManager: UserPrefsManager,
    private val dashboardRepository: DashboardRepository
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
                it.copy(selectedChartIndex = if (state.value.selectedChartIndex != action.index) action.index else null)
            }
        }

        DashboardAction.CloseBottomSheetClicked -> {
            _state.update { it.copy(isBottomSheetOpen = false) }
        }

        is DashboardAction.CalculateButtonClicked -> {


            action.amount?.let {
                val incomePercent = ((action.amount / state.value.monthlyIncome)*100).toDouble()
                val desirePercent = ((action.amount / state.value.desireBudget)*100).toDouble()
                val  workHours =  (action.amount / (state.value.monthlyIncome / state.value.monthlyWorkHours)).toFloat()
                val   remainingDesire =  (state.value.desireBudget - action.amount).toInt()
                val  currencySymbol = currencySymbols.getValue(state.value.selectedCurrency)
                _state.update {
                    it.copy(
                        evaluationResult = EvaluationResult(
                            incomePercent = incomePercent,
                            desirePercent = desirePercent,
                            workHours = workHours,
                            remainingDesire = remainingDesire,
                            currencySymbol = currencySymbol,
                        ),
                    )
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
                monthlyIncome = userPrefsManager.totalIncome.first().toDouble(),
                selectedCurrency = userPrefsManager.selectedCurrency.first().symbol,
                monthlyWorkHours = userPrefsManager.weeklyWorkHours.first() * 4.33f,
                desireBudget = 10000.0,
                monthlyExpense = 27000.0,
                savingProgress = 0.36f,
                isLoading = false,
                fixedExpenseFraction = 0.25f,
                desiresSpentFraction = 0.45f,
                expensesFraction = 0.3f,
                last6MonthAmounts = listOf(
                    MonthlyAmount("OCAK", 0.4f),
                    MonthlyAmount("SUBAT", 0.2f),
                    MonthlyAmount("MART", 0.3f),
                )
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

        }.launchIn(viewModelScope)

    }





}
