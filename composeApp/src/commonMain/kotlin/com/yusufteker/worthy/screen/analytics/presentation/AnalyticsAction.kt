package com.yusufteker.worthy.screen.analytics.presentation

import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.screen.analytics.domain.model.TimePeriod
import com.yusufteker.worthy.screen.analytics.presentation.components.ChartType
import com.yusufteker.worthy.screen.analytics.presentation.components.SortOption

sealed interface AnalyticsAction {
    object Init : AnalyticsAction
    data class OnPeriodSelected(val period: TimePeriod) : AnalyticsAction
    data class OnCategorySelected(val category: Category, val isSelected: Boolean) : AnalyticsAction
    data class OnCardSelected(val card: Card, val isSelected: Boolean) : AnalyticsAction
    object ClearFilters : AnalyticsAction
    object NavigateBack : AnalyticsAction

    data class OnItemDelete(val id: Int) : AnalyticsAction

    data class OnChangeViewMode(val viewMode: AnalyticsViewMode) : AnalyticsAction

    data class OnChangeChartType(val chartType: ChartType) : AnalyticsAction

    object OnAddTransactionClicked : AnalyticsAction

    data class OnSortSelected(val sortOption: SortOption) : AnalyticsAction

    data class OnTransactionTypeSelected(val transactionType: TransactionType?) : AnalyticsAction


}