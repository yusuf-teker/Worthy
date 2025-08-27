package com.yusufteker.worthy.screen.subscription.list.presentation

import com.yusufteker.worthy.core.domain.model.RecurringItem

sealed interface SubscriptionListAction {
    object Init : SubscriptionListAction
    object OnNavigateBack : SubscriptionListAction
    object OnAddNewSubscription : SubscriptionListAction
    object NavigateToAddSubscription : SubscriptionListAction

    data class OnItemDelete(val id: Int) : SubscriptionListAction

    data class OnItemClicked(val subscriptionId: Int) : SubscriptionListAction





}