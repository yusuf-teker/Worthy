package com.yusufteker.worthy.screen.subscription.list.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.subscription.domain.model.isActive
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SubscriptionListViewModel(
    private val subscriptionRepository: SubscriptionRepository
) : BaseViewModel<SubscriptionListState>(SubscriptionListState()) {

    init {
        observeData()
    }
    fun observeData(){
        launchWithLoading {
            subscriptionRepository.getAllSubscriptions() .onEach { subscriptions ->
                _state.update { currentState ->
                    currentState.copy(
                        activeSubscriptions = subscriptions.filter { it.isActive() },
                        inactiveSubscriptions = subscriptions.filter { !it.isActive() }
                    )
                }
            }.launchIn(viewModelScope)
        }

    }
    fun onAction(action: SubscriptionListAction) {
        when (action) {
            is SubscriptionListAction.Init -> {
                // TODO
            }

            is SubscriptionListAction.OnNavigateBack -> {
                navigateBack()
            }

            is SubscriptionListAction.OnAddNewSubscription -> {
                navigateTo(Routes.AddSubscription)
            }
            is SubscriptionListAction.NavigateToAddSubscription -> {
                navigateTo(Routes.AddSubscription)
            }
            is SubscriptionListAction.OnItemDelete ->{
                launchWithLoading {
                    subscriptionRepository.deleteSubscription(action.id)
                }

            }

        }
    }
}