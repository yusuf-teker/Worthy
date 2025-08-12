package com.yusufteker.worthy.screen.settings.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.domain.model.startDate
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.RecurringFinancialItemRepository
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.component1
import kotlin.collections.component2

class SettingsViewModel(
    private val userPrefsManager: UserPrefsManager,
    private val categoryRepository: CategoryRepository,
    private val recurringRepository: RecurringFinancialItemRepository,
    private val currencyConverter: CurrencyConverter,
) : BaseViewModel() {


    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    fun calculateTotalFixedIncome(){
        viewModelScope.launch {

            _state.update { currentState ->
                currentState.copy(
                    totalFixedIncome = state.value.uniqueIncomeRecurringItems
                        .groupBy { it.groupId } // groupId’ye göre grupla
                        .map { (_, items) ->
                            items.maxByOrNull { it.startDate() } // en güncel tarihli item
                        }
                        .filterNotNull()
                        .sumOf {
                            currencyConverter.convert(
                                it.amount,
                                state.value.selectedCurrency
                            ).amount


                        }
                )
            }
            if (state.value.totalFixedExpenses > state.value.totalFixedIncome){
                updateBudgetIfNeeded()
            }
        }
    }

    fun calculateTotalFixedExpenses(){
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    totalFixedExpenses = state.value.uniqueExpenseRecurringItems
                        .groupBy { it.groupId } // groupId’ye göre grupla
                        .map { (_, items) ->
                            items.maxByOrNull { it.startDate() } // en güncel tarihli item
                        }
                        .filterNotNull()
                        .sumOf {
                            currencyConverter.convert(
                                it.amount,
                                state.value.selectedCurrency
                            ).amount
                        }
                )
            }
            if (state.value.totalFixedExpenses > state.value.totalFixedIncome){
                 updateBudgetIfNeeded()
            }

        }

    }

    init {
        observeUserPreferences()
        observeData()

    }

    private fun observeData() {
        combine(
            categoryRepository.getAll(),
            recurringRepository.getAll(),
        ) {  categories, recurringItems ->
            _state.update { currentState ->
                currentState.copy(
                    categories = categories,
                    incomeRecurringItems = recurringItems.filter { it.isIncome },
                    expenseRecurringItems = recurringItems.filter { !it.isIncome },
                    uniqueIncomeRecurringItems = recurringItems.filter { it.isIncome }
                        .groupBy { it.groupId }.values.map { itemsInGroup ->
                            itemsInGroup.maxByOrNull { it.startDate() }!! },
                    uniqueExpenseRecurringItems = recurringItems.filter { !it.isIncome }
                        .groupBy { it.groupId }.values.map { itemsInGroup ->
                            itemsInGroup.maxByOrNull { it.startDate() }!! },

                )
            }

            calculateAll()


        }.launchIn(viewModelScope)
    }

    private fun  calculateSavings(){
        viewModelScope.launch {
            _state.update { currentState ->
                val savings = state.value.totalFixedIncome - state.value.totalFixedExpenses - currencyConverter.convert(money = state.value.budgetAmount, to = state.value.selectedCurrency).amount
                currentState.copy(
                    savingsAmount = savings
                )

            }
        }

    }

    private fun updateBudgetIfNeeded() {
        viewModelScope.launch {
            val expenseSum: Double = state.value.uniqueExpenseRecurringItems.sumOf {
                currencyConverter.convert(
                    it.amount,
                    state.value.selectedCurrency
                ).amount
            }
            val incomeSum: Double = state.value.uniqueIncomeRecurringItems.sumOf {
                currencyConverter.convert(
                    it.amount,
                    state.value.selectedCurrency
                ).amount
            }
            val budgetAmount = currencyConverter.convert(
                state.value.budgetAmount,
                to = state.value.selectedCurrency
            ).amount
            if (expenseSum >= incomeSum){
                updateBudgetAmount ( state.value.budgetAmount.copy(amount = 0.0) )
            }else if (state.value.budgetAmount.amount < 0){
                // Gelir daha yüksek + budget -'de kalmıs
                updateBudgetAmount(state.value.budgetAmount.copy(amount = 0.0))
            }else if ( (expenseSum + budgetAmount )> incomeSum ){
                // Gelir daha yüksek + budget +'de kalmıs
                updateBudgetAmount(
                    state.value.budgetAmount.copy(
                        amount = incomeSum - expenseSum
                    )
                )

            }

        }

    }

    private fun observeUserPreferences() {
        combine(
            userPrefsManager.desireBudget,
            userPrefsManager.weeklyWorkHours,
            userPrefsManager.selectedCurrency
        ) {  budgetAmount, weeklyWorkHours, selectedCurrency ->
            _state.update { currentState ->
                currentState.copy(
                    budgetAmount =
                        currencyConverter.convert(
                            budgetAmount ?: emptyMoney(),
                            to = selectedCurrency
                        ),
                    weeklyWorkHours = weeklyWorkHours,
                    selectedCurrency = selectedCurrency
                )
            }
            calculateAll()
        }.launchIn(viewModelScope)
    }

    fun  calculateAll(){// TODO BAKILACAK OPTİMİZASYON
        calculateTotalFixedIncome()
        calculateTotalFixedExpenses()
        //updateBudgetIfNeeded()

        calculateSavings()
    }
    fun onAction(action: SettingsAction){
        when(action){
            is SettingsAction.OnBudgetValueChange -> {
                updateBudgetAmount(action.newBudget)
                calculateSavings()
            }


            is SettingsAction.OnWeeklyWorkHoursChange -> {

                Napier.d { "Weekly work hours changed: ${action.hours}" }
                updateWeeklyWorkHours(action.hours)

            }

            is SettingsAction.OnCurrencyChange -> {
                updateCurrency(action.currency)

            }

            is SettingsAction.OnSaveRecurringItems -> {



            }

            is SettingsAction.OnAddNewRecurringItem -> {
                viewModelScope.launch {
                    try {
                        recurringRepository.add(action.item)
                    } catch (e: Exception) {
                        handleError(e)
                    }
                }


            }
            is SettingsAction.OnDeleteRecurringItem -> {
                viewModelScope.launch {
                    try {
                        recurringRepository.delete(action.item)
                    } catch (e: Exception) {
                        handleError(e)
                    }
                }
            }
            is SettingsAction.OnUpdateRecurringItems -> {
                viewModelScope.launch {
                    try {
                        recurringRepository.updateGroup(action.items)
                    } catch (e: Exception) {
                        handleError(e)
                    }
                }
            }
            is SettingsAction.OnDeleteGroupRecurringItem -> {

                viewModelScope.launch {
                    try {
                        recurringRepository.deleteGroup(action.groupId)
                    } catch (e: Exception) {
                        handleError(e)
                    }
                }
            }
        }

    }



    private fun updateCurrency(currency: Currency) {
        viewModelScope.launch {
            try {
                userPrefsManager.setSelectedCurrency(currency)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun updateWeeklyWorkHours(hours: Int) {
        viewModelScope.launch {
            try {
                userPrefsManager.setWeeklyWorkHours(hours)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun updateBudgetAmount(amount: Money) {
        viewModelScope.launch {
            try {
                userPrefsManager.setBudgetMoney(amount)
            } catch (e: Exception) {
                handleError(e)
            }

        }
    }

    private fun handleError(error: Exception) {
    }
}