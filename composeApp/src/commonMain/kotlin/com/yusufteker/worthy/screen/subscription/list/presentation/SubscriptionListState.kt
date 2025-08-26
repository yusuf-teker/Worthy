package com.yusufteker.worthy.screen.subscription.list.presentation

import com.yusufteker.worthy.core.domain.model.RecurringItem
import com.yusufteker.worthy.core.presentation.base.BaseState

data class SubscriptionListState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val activeSubscriptions: List<RecurringItem.Subscription> = emptyList(),
    val inactiveSubscriptions: List<RecurringItem.Subscription> = emptyList()
) : BaseState {
    override fun copyWithLoading(isLoading: Boolean): BaseState {
        return this.copy(isLoading = isLoading)
    }
}