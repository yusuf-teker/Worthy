        package com.yusufteker.worthy.screen.subscriptiondetail.presentation
        
        import com.yusufteker.worthy.core.domain.getCurrentAppDate
        import com.yusufteker.worthy.core.domain.model.AppDate
        import com.yusufteker.worthy.core.domain.model.RecurringItem
        import com.yusufteker.worthy.core.presentation.base.BaseState

        data class SubscriptionDetailState(
            override val isLoading: Boolean = false,
            val errorMessage: String? = null,
            val subscription: RecurringItem.Subscription? = null,
            val pickedDate: AppDate = getCurrentAppDate()
        ): BaseState{
            override fun copyWithLoading(isLoading: Boolean): BaseState {
            return this.copy(isLoading = isLoading)
        }
}