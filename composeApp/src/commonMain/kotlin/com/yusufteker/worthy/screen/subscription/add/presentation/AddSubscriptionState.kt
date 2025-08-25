package com.yusufteker.worthy.screen.subscription.add.presentation

import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.base.BaseState
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.screen.subscription.domain.model.Subscription
import com.yusufteker.worthy.screen.subscription.domain.model.SubscriptionCategory
import com.yusufteker.worthy.screen.subscription.domain.model.emptySubscription

data class AddSubscriptionState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null,

    var subscriptionName: String = "",
    val selectedCategory: SubscriptionCategory? = null,
    val categories: List<SubscriptionCategory> = SubscriptionCategory.entries,
    val color: String? = "",
    val customCategoryName: String = "",
    val selectedEmoji: String? = null,
    val price: Money? = emptyMoney(),
    val startDate: AppDate = AppDate(getCurrentYear(), getCurrentMonth()),
    val endDate: AppDate? = null,
    val scheduledDay: Int = 1,
    val cards: List<Card> = emptyList(),
    val selectedCard: Card? = null,

    val subscriptionPrev: Subscription = emptySubscription
) : BaseState {
    override fun copyWithLoading(isLoading: Boolean): BaseState {
        return this.copy(isLoading = isLoading)
    }
}