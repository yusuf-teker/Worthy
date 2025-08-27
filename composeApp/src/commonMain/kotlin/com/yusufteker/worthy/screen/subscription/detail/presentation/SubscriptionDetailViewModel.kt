package com.yusufteker.worthy.screen.subscriptiondetail.presentation

import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.update

class SubscriptionDetailViewModel(
    private val subscriptionRepository: SubscriptionRepository
) : BaseViewModel<SubscriptionDetailState>(SubscriptionDetailState()) {


    fun onAction(action: SubscriptionDetailAction) {
        when (action) {
            is SubscriptionDetailAction.Init -> {
                launchWithLoading {
                    val subscription = subscriptionRepository.getSubscriptionById(action.subscriptionId)
                    setState { copy(subscription = subscription) }
                }
            }
            is SubscriptionDetailAction.NavigateBack -> {
                navigateBack()
            }

            is SubscriptionDetailAction.ActivateSubscription -> TODO()
            is SubscriptionDetailAction.EndSubscription -> TODO()
            is SubscriptionDetailAction.OnDateSelected -> {
                _state.update { it.copy(pickedDate = action.date) }
            }
        }
    }


}