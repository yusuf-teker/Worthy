package com.yusufteker.worthy.screen.settings.presentation

import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.RecurringItem

sealed interface SettingsAction {
    data class OnBudgetValueChange(val newBudget: Money) : SettingsAction
    data class OnWeeklyWorkHoursChange(val hours: Int) : SettingsAction
    data class OnCurrencyChange(val currency: Currency) : SettingsAction

    data class OnSaveRecurringItems(val items: List<RecurringItem.Generic>) : SettingsAction

    data class OnDeleteRecurringItem(val item: RecurringItem.Generic) : SettingsAction
    data class OnUpdateRecurringItems(val items: List<RecurringItem.Generic>) : SettingsAction
    data class OnAddNewRecurringItem(val item: RecurringItem.Generic) : SettingsAction

    data class OnDeleteGroupRecurringItem(val groupId: String) : SettingsAction

    object OnMyCardsClick : SettingsAction
    object OnSubscriptionsClick : SettingsAction


}