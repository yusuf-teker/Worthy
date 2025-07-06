package com.yusufteker.worthy.feature.settings.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.feature.settings.domain.ExpenseItem
import com.yusufteker.worthy.feature.settings.domain.IncomeItem
import com.yusufteker.worthy.feature.settings.domain.UserPrefsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPrefsManager: UserPrefsManager
) : BaseViewModel() {


    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    init {
        observeUserPreferences()
    }

    private fun observeUserPreferences() {
        combine(
            userPrefsManager.incomeItems,
            userPrefsManager.expenseItems,
            userPrefsManager.budgetAmount
        ) { incomeItems, expenseItems, budgetAmount ->
            _state.update { currentState ->
                currentState.copy(
                    incomeItems = incomeItems,
                    fixedExpenseItems = expenseItems,
                    budgetAmount = budgetAmount
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

    private fun saveExpenseItems(items: List<ExpenseItem>) {
        viewModelScope.launch {
            try {
                userPrefsManager.setExpenseItems(items)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun saveIncomeItems(items: List<IncomeItem>) {
        viewModelScope.launch {
            try {
                userPrefsManager.setIncomeItems(items)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }
    private fun handleError(error: Exception) {
    }
}