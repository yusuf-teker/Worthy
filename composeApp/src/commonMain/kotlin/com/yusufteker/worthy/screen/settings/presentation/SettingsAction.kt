package com.yusufteker.worthy.screen.settings.presentation

import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem

sealed interface SettingsAction {
    data class OnBudgetValueChange(val newBudget: Money) : SettingsAction
    data class OnWeeklyWorkHoursChange(val hours: Int) : SettingsAction
    data class OnCurrencyChange(val currency: Currency) : SettingsAction

    data class OnSaveRecurringItems(val items: List<RecurringFinancialItem>) : SettingsAction

    data class OnDeleteRecurringItem(val item: RecurringFinancialItem) : SettingsAction
    data class OnUpdateRecurringItems(val items: List<RecurringFinancialItem>) : SettingsAction
    data class OnAddNewRecurringItem(val item: RecurringFinancialItem) : SettingsAction

    data class OnDeleteGroupRecurringItem(val groupId: String) : SettingsAction

    object OnMyCardsClick : SettingsAction


}