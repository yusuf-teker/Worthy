package com.yusufteker.worthy.feature.settings.presentation

import com.yusufteker.worthy.feature.settings.domain.ExpenseItem
import com.yusufteker.worthy.feature.settings.domain.IncomeItem

sealed interface SettingsAction {
    data class OnBudgetValueChange(val newBudget: Float) : SettingsAction
    data class OnSaveIncomeItems(val items: List<IncomeItem>) : SettingsAction
    data class OnSaveExpenseItems(val items: List<ExpenseItem>) : SettingsAction
    data class OnWeeklyWorkHoursChange(val hours: Int) : SettingsAction
    data class OnCurrencyChange(val currency: String) : SettingsAction



}