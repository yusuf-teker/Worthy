package com.yusufteker.worthy.screen.subscription.add.presentation

import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.RecurringItem
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.domain.model.emptySubscription
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseState
import com.yusufteker.worthy.screen.card.domain.model.Card

data class AddSubscriptionState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null,

    var subscriptionName: String = "",
    val selectedCategory: Category? = null,
    val categories: List<Category> = emptyList(),
    val color: String? = "",
    val customCategoryName: String = "",
    val selectedEmoji: String? = null,
    val price: Money? = emptyMoney(),
    val startDate: AppDate = getCurrentAppDate().copy(day = 1),
    val endDate: AppDate? = null,
    val scheduledDay: Int = 1,
    val cards: List<Card> = emptyList(),
    val selectedCard: Card? = null,
    val errorName: UiText? = null,
    val errorCategory: UiText? = null,
    val errorPrice: UiText? = null,


    val subscriptionPrev: RecurringItem.Subscription = emptySubscription
) : BaseState {
    override fun copyWithLoading(isLoading: Boolean): BaseState {
        return this.copy(isLoading = isLoading)
    }
}

fun AddSubscriptionState.hasError(): Boolean {
    return errorName != null ||
            errorCategory != null ||
            errorPrice != null
}