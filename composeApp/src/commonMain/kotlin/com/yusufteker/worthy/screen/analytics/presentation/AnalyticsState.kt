        package com.yusufteker.worthy.screen.analytics.presentation
        
        import com.yusufteker.worthy.core.domain.model.Card
        import com.yusufteker.worthy.core.domain.model.Category
        import com.yusufteker.worthy.core.domain.model.Transaction
        import com.yusufteker.worthy.core.presentation.base.BaseState
        import com.yusufteker.worthy.screen.analytics.presentation.components.DateRange

        data class AnalyticsState(
            override val isLoading: Boolean = false,
            val errorMessage: String? = null,
            val categories: List<Category> = emptyList(),
            val cards: List<Card> = emptyList(),
            val selectedCategories: List<Category> = emptyList(),
            val selectedCards: List<Card> = emptyList(),
            val selectedDateRange: DateRange = DateRange.ALL_TIME,

            val transactions: List<Transaction> = emptyList()

        ): BaseState{
            override fun copyWithLoading(isLoading: Boolean): BaseState {
            return this.copy(isLoading = isLoading)
        }
}