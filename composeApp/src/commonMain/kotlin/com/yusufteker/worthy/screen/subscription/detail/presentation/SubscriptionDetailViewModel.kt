package com.yusufteker.worthy.screen.subscription.detail.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import com.yusufteker.worthy.screen.subscriptiondetail.presentation.SubscriptionDetailAction
import com.yusufteker.worthy.screen.subscriptiondetail.presentation.SubscriptionDetailState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SubscriptionDetailViewModel(
    private val subscriptionRepository: SubscriptionRepository
) : BaseViewModel<SubscriptionDetailState>(SubscriptionDetailState()) {



    fun observeData(groupId: String){
        launchWithLoading {
            subscriptionRepository.getAllSubscriptionsByGroupId(groupId) .onEach { subscriptions ->

                    setState { copy(
                        subscription = subscriptions.firstOrNull(),
                        subscriptions = subscriptions
                    ) }

            }.launchIn(viewModelScope)
        }

    }

    fun onAction(action: SubscriptionDetailAction) {
        when (action) {
            is SubscriptionDetailAction.Init -> {
                launchWithLoading {
                    val subscription = subscriptionRepository.getSubscriptionById(action.subscriptionId)
                    subscription?.groupId?.let {
                        observeData(it)
                    }

                }
            }
            is SubscriptionDetailAction.NavigateBack -> {
                navigateBack()
            }

            is SubscriptionDetailAction.ActivateSubscription -> {

                launchWithLoading {
                    val today = getCurrentAppDate(day = 1)
                    val pickedDate = state.value.pickedDate
                    val subscriptions = state.value.subscriptions
                    val isActive = subscriptions.firstOrNull{it.endDate == null}
                    val maxEndDate = subscriptions.mapNotNull { it.endDate }.max()
                    if (isActive != null && isActive.startDate <= today){ // end date yok devam ediyor
                        // error mesajı göster devam eden abonelik
                    }else if (pickedDate <= maxEndDate){
                        // başlangıç günü eski tarihler ile uyuşmuyor
                    }
                    else if (pickedDate > maxEndDate) {
                        val subscription = state.value.subscription ?: return@launchWithLoading
                        val newItem = subscription.copy(
                            id = 0,
                            startDate = pickedDate,
                            endDate = null
                        )
                        subscriptionRepository.addSubscription(newItem)
                    }

                }

                // Handle activation logic
            }
            is SubscriptionDetailAction.EndSubscription -> {

                // Handle termination logic
                launchWithLoading {

                    val today = getCurrentAppDate()

                    val activeItem = state.value.subscriptions
                        .firstOrNull { it.endDate == null || it.endDate > today }

                    activeItem?.let { item ->
                        val updated = item.copy(endDate = today)
                        subscriptionRepository.updateGroup(
                            state.value.subscriptions.map {
                                if (it.id == item.id) updated else it
                            },
                            state.value.subscriptions
                        )
                    }
                }
            }
            is SubscriptionDetailAction.OnDateSelected -> {
                _state.update { it.copy(pickedDate = action.date) }
            }


            is SubscriptionDetailAction.OnAddNewRecurringItem -> {
                launchWithLoading {
                    subscriptionRepository.addSubscription(action.item)
                }
            }
            is SubscriptionDetailAction.OnDeleteGroupRecurringItem -> {
                launchWithLoading {
                    subscriptionRepository.deleteByGroupId(action.groupId)
                }
            }
            is SubscriptionDetailAction.OnUpdateRecurringItems -> {
                launchWithLoading {
                    subscriptionRepository.updateGroup(action.items,state.value.subscriptions)

                }
            }
        }
    }

}