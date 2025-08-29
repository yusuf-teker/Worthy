package com.yusufteker.worthy.screen.subscriptiondetail.presentation

import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.RecurringItem

sealed interface SubscriptionDetailAction {
    data class Init(val subscriptionId: Int) : SubscriptionDetailAction
    object NavigateBack : SubscriptionDetailAction
    data class OnDateSelected(val date: AppDate) : SubscriptionDetailAction
    data class ActivateSubscription(val date: AppDate) : SubscriptionDetailAction
    data class EndSubscription(val date: AppDate) : SubscriptionDetailAction


    data class OnUpdateRecurringItems(val items: List<RecurringItem.Subscription>): SubscriptionDetailAction

    data class OnDeleteGroupRecurringItem(val groupId: String): SubscriptionDetailAction

    data class OnAddNewRecurringItem(val item: RecurringItem.Subscription): SubscriptionDetailAction




}