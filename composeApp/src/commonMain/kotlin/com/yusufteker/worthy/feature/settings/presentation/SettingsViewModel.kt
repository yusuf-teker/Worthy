package com.yusufteker.worthy.feature.settings.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.ExpenseRepository
import com.yusufteker.worthy.core.domain.repository.IncomeRepository
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

class SettingsViewModel(
    private val userPrefsManager: UserPrefsManager,
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {


    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    init {
        observeUserPreferences()
        observeData()

    }

    private fun observeData() {
        combine(
            incomeRepository.getAll(),
            expenseRepository.getAll(),
            categoryRepository.getAll()
        ) { incomes, expenses, categories ->
            _state.update { currentState ->
                currentState.copy(
                    incomeItems = incomes,
                    expenseItems = expenses,
                    categories = categories
                )
            }
        }.launchIn(viewModelScope)
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