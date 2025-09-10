package com.yusufteker.worthy.screen.subscription.detail.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.data.database.mappers.toTransactions
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.RecurringItem
import com.yusufteker.worthy.core.domain.model.isActive
import com.yusufteker.worthy.core.domain.model.monthsBetween
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import com.yusufteker.worthy.screen.subscriptiondetail.presentation.SubscriptionDetailAction
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.confirm
import worthy.composeapp.generated.resources.delete_subscription_confirm
import worthy.composeapp.generated.resources.delete_subscription_message
import worthy.composeapp.generated.resources.delete_subscription_title

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

                    state.value.subscription?.let {
                        if (it.isActive()){
                            setState { copy(
                                activeStreak = calculateSubscriptionStreak(subscriptions)

                            ) }
                        }
                    }

            }.launchIn(viewModelScope)
        }

    }


    fun calculateSubscriptionStreak(subscriptions: List<RecurringItem.Subscription>): Int {
        if (subscriptions.isEmpty()) return 0

        // Abonelikleri başlangıç tarihine göre sırala (en yeniden eskiye)
        val sortedSubscriptions = subscriptions.sortedByDescending { it.startDate }

        var totalStreakMonths = 0
        val currentDate = getCurrentAppDate() // Şu anki tarihi al

        // En son aktif aboneliği bul
        val latestSubscription = sortedSubscriptions.firstOrNull { subscription ->
            subscription.endDate == null || subscription.endDate!! >= currentDate
        }

        if (latestSubscription == null) {
            // Aktif abonelik yok, streak 0
            return 0
        }

        // Son aktif aboneliğin end date'ini belirle
        val latestEndDate = latestSubscription.endDate ?: currentDate

        // Geriye doğru kesintisiz streak hesapla
        var checkDate = latestEndDate

        for (subscription in sortedSubscriptions) {
            val subEndDate = subscription.endDate ?: currentDate
            val subStartDate = subscription.startDate

            // Bu abonelik check date ile kesintisiz bağlantılı mı?
            if (isMonthContinuous(subEndDate, checkDate)) {
                // Bu aboneliğin ay sayısını hesapla
                val monthsInSubscription = monthsBetween(subStartDate, subEndDate)
                totalStreakMonths += monthsInSubscription

                // Bir sonraki kontrol için check date'i güncelle
                checkDate = subStartDate
            } else {
                // Kesinti var, streak burada bitiyor
                break
            }
        }

        return totalStreakMonths
    }


    private fun isMonthContinuous(endDate: AppDate, checkDate: AppDate): Boolean {
        // Aynı ay/yıl ise kesintisiz
        if (endDate.year == checkDate.year && endDate.month == checkDate.month) {
            return true
        }

        // End date'in bir sonraki ayı check date ile aynı mı?
        val nextMonth = endDate.nextMonth()
        return nextMonth.year == checkDate.year && nextMonth.month == checkDate.month
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

                popupManager.showConfirm(
                    title = Res.string.delete_subscription_title,
                    message = Res.string.delete_subscription_message,
                    onConfirm = {
                        launchWithLoading {
                            subscriptionRepository.deleteByGroupId(action.groupId)
                            navigateBack()
                        }
                    },
                     confirmLabel =  UiText.StringResourceId(Res.string.delete_subscription_confirm),
                    dismissLabel = UiText.StringResourceId(Res.string.cancel)
                )

            }
            is SubscriptionDetailAction.OnUpdateRecurringItems -> {
                launchWithLoading {
                    subscriptionRepository.updateGroup(action.items,state.value.subscriptions)

                }
            }
        }
    }

}