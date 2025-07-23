package com.yusufteker.worthy.screen.settings.presentation

import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem

sealed interface SettingsAction {
    data class OnBudgetValueChange(val newBudget: Float) : SettingsAction
    data class OnSaveIncomeItems(val items: List<Income>) : SettingsAction
    data class OnSaveExpenseItems(val items: List<Expense>) : SettingsAction
    data class OnWeeklyWorkHoursChange(val hours: Int) : SettingsAction
    data class OnCurrencyChange(val currency: String) : SettingsAction

    data class OnSaveRecurringItems(val items: List<RecurringFinancialItem>) : SettingsAction

    data class OnDeleteRecurringItem(val item: RecurringFinancialItem) : SettingsAction
    data class OnUpdateRecurringItems(val items: List<RecurringFinancialItem>) : SettingsAction
    data class OnAddNewRecurringItem(val item: RecurringFinancialItem) : SettingsAction

    data class OnDeleteGroupRecurringItem(val groupId: String ) : SettingsAction



}