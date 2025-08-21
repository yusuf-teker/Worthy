package com.yusufteker.worthy.screen.analytics.presentation

import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.screen.analytics.presentation.components.DateRange

sealed interface AnalyticsAction {
    object Init : AnalyticsAction
    data class OnDateRangeSelected(val dateRange: DateRange): AnalyticsAction
    data class OnCategorySelected(val category: Category, val isSelected: Boolean): AnalyticsAction
    data class OnCardSelected(val card: Card, val isSelected: Boolean): AnalyticsAction
    object ClearFilters: AnalyticsAction
    object NavigateBack: AnalyticsAction

    data class OnItemDelete(val id: Int): AnalyticsAction



}