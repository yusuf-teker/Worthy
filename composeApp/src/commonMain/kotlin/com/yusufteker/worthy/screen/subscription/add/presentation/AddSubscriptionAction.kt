package com.yusufteker.worthy.screen.subscription.add.presentation

import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.screen.card.domain.model.Card

sealed interface AddSubscriptionAction {
    object Init : AddSubscriptionAction
    object NavigateBack : AddSubscriptionAction
    data class OnNameChanged(val name: String) : AddSubscriptionAction
    data class OnCategorySelected(val category: Category?) : AddSubscriptionAction
    data class OnNewCategoryCreated(val category: Category) : AddSubscriptionAction
    data class OnEmojiSelected(val emoji: String) : AddSubscriptionAction
    data class OnPriceChanged(val price: Money) : AddSubscriptionAction
    data class OnStartDateChanged(val date: AppDate) : AddSubscriptionAction
    data class OnEndDateChanged(val date: AppDate?) : AddSubscriptionAction
    data class OnScheduledDayChanged(val day: Int) : AddSubscriptionAction
    object SubmitSubscription : AddSubscriptionAction
    data class OnColorChanged(val color: String) : AddSubscriptionAction

    data class OnCardSelected(val card: Card) : AddSubscriptionAction


}