package com.yusufteker.worthy.feature.settings.presentation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.model.startDate
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.ExpenseRepository
import com.yusufteker.worthy.core.domain.repository.IncomeRepository
import com.yusufteker.worthy.core.domain.repository.RecurringFinancialItemRepository
import com.yusufteker.worthy.core.domain.repository.WishlistRepository
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.feature.settings.domain.UserPrefsManager
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
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val recurringRepository: RecurringFinancialItemRepository
) : BaseViewModel() {


    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    fun calculateTotalFixedIncome(){
        _state.update { currentState ->
            currentState.copy(
                totalFixedIncome = state.value.uniqueIncomeRecurringItems
                    .groupBy { it.groupId } // groupId’ye göre grupla
                    .map { (_, items) ->
                        items.maxByOrNull { it.startDate() } // en güncel tarihli item
                    }
                    .filterNotNull()
                    .sumOf { it.amount }
                    .toFloat()
            )
        }
    }

    fun calculateTotalFixedExpenses(){
        _state.update { currentState ->
            currentState.copy(
                totalFixedExpenses = state.value.uniqueExpenseRecurringItems
                    .groupBy { it.groupId } // groupId’ye göre grupla
                    .map { (_, items) ->
                        items.maxByOrNull { it.startDate() } // en güncel tarihli item
                    }
                    .filterNotNull()
                    .sumOf { it.amount }
                    .toFloat()
            )
        }
    }

    init {
        observeUserPreferences()
        observeData()

    }

    private fun observeData() {
        combine(
            categoryRepository.getAll(),
            recurringRepository.getAll()
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

            updateBudgetIfNeeded()
            calculateTotalFixedIncome()
            calculateTotalFixedExpenses()
            calculateSavings()


        }.launchIn(viewModelScope)
    }

    private fun  calculateSavings(){
        _state.update { currentState ->
            val savings = state.value.totalFixedIncome - state.value.totalFixedExpenses - state.value.budgetAmount
            currentState.copy(
                savingsAmount = savings.toFloat()
            )
        }
    }
    private fun observeUserPreferences() {
        combine(
            userPrefsManager.budgetAmount,
            userPrefsManager.weeklyWorkHours,
            userPrefsManager.selectedCurrency
        ) {  budgetAmount, weeklyWorkHours, selectedCurrency ->
            _state.update { currentState ->
                currentState.copy(
                    budgetAmount = budgetAmount,
                    weeklyWorkHours = weeklyWorkHours,
                    selectedCurrency = selectedCurrency
                )
            }
        }.launchIn(viewModelScope)
    }
    fun onAction(action: SettingsAction){
        when(action){
            is SettingsAction.OnBudgetValueChange -> {
                updateBudgetAmount(action.newBudget)
            }

            is SettingsAction.OnSaveExpenseItems -> {

                saveExpenseItems(action.items)

            }
            is SettingsAction.OnSaveIncomeItems -> {
                saveIncomeItems(action.items)
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



    private fun updateCurrency(currency: String) {
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

    private fun updateBudgetAmount(amount: Float) {
        viewModelScope.launch {
            try {
                userPrefsManager.setBudgetAmount(amount)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun updateBudgetIfNeeded() {
        val expenseSum: Double = state.value.uniqueExpenseRecurringItems.sumOf {
            it.amount
        }
        val incomeSum: Double = state.value.uniqueIncomeRecurringItems.sumOf {
            it.amount
        }
        if (expenseSum >= incomeSum){
            updateBudgetAmount (0f )
        }else if (state.value.budgetAmount < 0){
            // Gelir daha yüksek + budget -'de kalmıs
            updateBudgetAmount(0f)
        }
    }

    private fun saveExpenseItems(updatedFixedExpenses: List<Expense>) {
        viewModelScope.launch {
            try {
                val currentFixed = expenseRepository.getFixed()
                val newIds = updatedFixedExpenses.map { it.id }.toSet()

                val toDelete = currentFixed.filter { it.id !in newIds }

                val toInsertOrUpdate = updatedFixedExpenses

                expenseRepository.deleteAll(toDelete)
                expenseRepository.insertAll(toInsertOrUpdate)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun saveIncomeItems(updatedFixedIncomes: List<Income>) {
        viewModelScope.launch {
            try {
                val currentFixed = incomeRepository.getFixed()
                val newIds = updatedFixedIncomes.map { it.id }.toSet()

                val toDelete = currentFixed.filter { it.id !in newIds }
                val toInsertOrUpdate = updatedFixedIncomes

                incomeRepository.deleteAll(toDelete)
                incomeRepository.insertAll(toInsertOrUpdate)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }
    private fun handleError(error: Exception) {
    }
}