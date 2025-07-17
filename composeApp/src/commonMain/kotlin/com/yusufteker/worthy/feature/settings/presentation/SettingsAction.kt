package com.yusufteker.worthy.feature.settings.presentation

import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income

sealed interface SettingsAction {
    data class OnBudgetValueChange(val newBudget: Float) : SettingsAction
    data class OnSaveIncomeItems(val items: List<Income>) : SettingsAction
    data class OnSaveExpenseItems(val items: List<Expense>) : SettingsAction
    data class OnWeeklyWorkHoursChange(val hours: Int) : SettingsAction
    data class OnCurrencyChange(val currency: String) : SettingsAction



}