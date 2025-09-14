package com.yusufteker.worthy.screen.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.getCurrentLocalDateTime
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.getLastMonth
import com.yusufteker.worthy.core.domain.model.sumConvertedAmount
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.core.presentation.UiEvent.NavigateTo
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.core.presentation.util.emptyMoney
import com.yusufteker.worthy.core.presentation.util.hideKeyboard
import com.yusufteker.worthy.screen.dashboard.domain.helper.DashboardBarChartCalculator
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRepository
import com.yusufteker.worthy.screen.dashboard.domain.EvaluateExpenseUseCase
import com.yusufteker.worthy.screen.dashboard.domain.EvaluationResult
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userPrefsManager: UserPrefsManager,
    private val dashboardRepository: DashboardRepository,
    private val currencyConverter: CurrencyConverter
) : BaseViewModel<DashboardState>(DashboardState()) {

    // TODO
    // 1 TAKSITLI İŞLEMLER MİNİ BAR CHARTTA GÖZÜKMYÜRO

    init {
        observeData()
    }

    val calculator = DashboardBarChartCalculator(currencyConverter)
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
            val evaluateExpenseUseCase = EvaluateExpenseUseCase()

            launchWithLoading {

                _state.update { it.updateBottomSheet { copy(isCalculating = true) } }

                val monthlyIncome = currencyConverter.convert(state.value.totalAllIncomeMoney, to = action.money.currency)
                val monthlyExpense = currencyConverter.convert(state.value.totalAllExpenseMoney, to = action.money.currency)
                val convertedDesireBudget = currencyConverter.convert(state.value.desireBudget, to = action.money.currency)

                val result = evaluateExpenseUseCase(
                    money = action.money,
                    monthlyIncome = monthlyIncome,
                    monthlyExpense = monthlyExpense,
                    desireBudget = convertedDesireBudget,
                    monthlyWorkHours = state.value.monthlyWorkHours,
                    selectedCurrencySymbol = state.value.selectedCurrency.symbol
                )

                _state.update {
                    it.updateBottomSheet {
                        copy(
                            evaluationResult = result,
                            isCalculating = false
                        )
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
            sendNavigationEventSafe(NavigateTo(Routes.Settings))
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
                _state.update {
                    it.updateBottomSheet { copy(isPurchasing = false) }.copy(
                        isBottomSheetOpen = false
                    )
                }
            }
        }

        is DashboardAction.onInstallmentsMenuClicked -> {
            navigateTo(Routes.InstallmentGraph)
        }
    }

    private fun observeData() {

        launchWithLoading {

            _state.update {
                it.copy(
                    userName = userPrefsManager.userName.first() ?: "",
                    selectedCurrency = userPrefsManager.selectedCurrency.first(),
                    monthlyWorkHours = userPrefsManager.weeklyWorkHours.first() * 4.33f,
                    desireBudget = userPrefsManager.desireBudget.first() ?: emptyMoney(
                        userPrefsManager.selectedCurrency.first()
                    ),
                    savingProgress = 0.36f,
                )
            }


            combine(
                dashboardRepository.getAllExpenseMonthlyAmount(6, getCurrentLocalDateTime()),
                dashboardRepository.getAllIncomeMonthlyAmount(6, getCurrentLocalDateTime()),
                dashboardRepository.getAllRecurringMonthlyAmount(6, getCurrentLocalDateTime()),
                dashboardRepository.getAllWishlistMonthlyAmount(6, getCurrentLocalDateTime()),
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
            }
                // burada sadece ilk değeri bekliyoruz
                .first()

        }

    }

    private suspend fun calculateBarRatios(
        expenses: List<DashboardMonthlyAmount>, // LAST 6 MONTHS DATA
        incomes: List<DashboardMonthlyAmount>,
        recurringIncomes: List<DashboardMonthlyAmount>,
        recurringExpenses: List<DashboardMonthlyAmount>,
        wishlistItems: List<DashboardMonthlyAmount>
    ) {

        val result = calculator.calculate(
            expenses,
            incomes,
            recurringIncomes,
            recurringExpenses,
            wishlistItems,
            state.value.selectedCurrency,
            state.value.selectedMonthYear
        )

        _state.update { currentState ->
            currentState.copy(
                fixedExpenseMoney = result.fixedExpenseMoney,
                desiresSpentMoney = result.desiresSpentMoney,
                remainingMoney = result.remainingMoney,
                expensesMoney = result.expensesMoney,
                expensesFraction = result.expensesFraction,
                fixedExpenseFraction = result.fixedExpenseFraction,
                desiresSpentFraction = result.desiresSpentFraction,
                remainingFraction = result.remainingFraction,
                totalAllIncomeMoney = result.totalAllIncomeMoney,
                totalAllExpenseMoney = result.totalAllExpenseMoney,
                miniBarsFractions = result.miniBarsFractions,
                miniBarsMonths = result.miniBarsMonths,
                incomeChangeRatio = calculateSelectedMonthIncomeChangeRatio(state.value.selectedMonthYear)

            )
        }

    }

    private suspend fun calculateSelectedMonthRecurringIncome(month: AppDate): Money {
        return state.value.recurringIncomeMonthlyAmountList.sumConvertedAmount(
            state.value.selectedCurrency, month, currencyConverter
        ) ?: emptyMoney(state.value.selectedCurrency)
    }

    private suspend fun calculateSelectedMonthIncome(month: AppDate): Money {
        return state.value.incomeMonthlyAmountList.sumConvertedAmount(
            state.value.selectedCurrency, month, currencyConverter
        ) ?: emptyMoney(state.value.selectedCurrency)
    }

    private suspend fun calculateSelectedMonthRecurringExpense(month: AppDate): Money {
        return state.value.recurringExpenseMonthlyAmountList.sumConvertedAmount(
            state.value.selectedCurrency, month, currencyConverter
        ) ?: emptyMoney(state.value.selectedCurrency)
    }

    private suspend fun calculateSelectedMonthExpense(month: AppDate): Money {
        return state.value.expenseMonthlyAmountList.sumConvertedAmount(
            state.value.selectedCurrency, month, currencyConverter
        ) ?: emptyMoney(state.value.selectedCurrency)
    }

    private suspend fun calculateSelectedMonthAllIncome(month: AppDate): Money {
        return Money(
            calculateSelectedMonthIncome(month).amount + calculateSelectedMonthRecurringIncome(
                month
            ).amount, state.value.selectedCurrency
        )
    }

    private suspend fun calculateSelectedMonthAllExpense(month: AppDate): Money {
        return Money(
            calculateSelectedMonthExpense(month).amount + calculateSelectedMonthRecurringExpense(
                month
            ).amount, state.value.selectedCurrency
        )
    }

    private suspend fun calculateSelectedMonthIncomeChangeRatio(month: AppDate): Double {
        val lastMonthIncome =
            calculateSelectedMonthRecurringIncome(month.getLastMonth()).amount + calculateSelectedMonthIncome(
                month.getLastMonth()
            ).amount
        val selectedMonthIncome =
            (calculateSelectedMonthRecurringIncome(month).amount) + calculateSelectedMonthIncome(
                month
            ).amount

        return if (lastMonthIncome == 0.0 && selectedMonthIncome == 0.0) {
            0.0
        } else if (lastMonthIncome != 0.0) {
            (selectedMonthIncome - lastMonthIncome) / lastMonthIncome * 100
        } else {
            100.0
        }
    }

}





