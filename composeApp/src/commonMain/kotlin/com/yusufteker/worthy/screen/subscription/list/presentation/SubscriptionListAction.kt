package com.yusufteker.worthy.screen.subscription.list.presentation

sealed interface SubscriptionListAction {
    object Init : SubscriptionListAction
    object OnNavigateBack : SubscriptionListAction
    object OnAddNewSubscription : SubscriptionListAction
    object NavigateToAddSubscription : SubscriptionListAction

    data class OnItemDelete(val id: Int) : SubscriptionListAction




}