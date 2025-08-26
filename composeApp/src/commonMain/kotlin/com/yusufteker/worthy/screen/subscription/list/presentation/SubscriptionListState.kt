package com.yusufteker.worthy.screen.subscription.list.presentation

import com.yusufteker.worthy.core.presentation.base.BaseState
import com.yusufteker.worthy.screen.subscription.domain.model.Subscription

data class SubscriptionListState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val activeSubscriptions: List<Subscription> = emptyList(),
    val inactiveSubscriptions: List<Subscription> = emptyList()
) : BaseState {
    override fun copyWithLoading(isLoading: Boolean): BaseState {
        return this.copy(isLoading = isLoading)
    }
}