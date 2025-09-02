package com.yusufteker.worthy.screen.analytics.presentation

import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.presentation.base.BaseState
import com.yusufteker.worthy.screen.analytics.domain.model.TimePeriod
import com.yusufteker.worthy.screen.analytics.presentation.components.ChartType
import com.yusufteker.worthy.screen.analytics.presentation.components.SortOption

enum class AnalyticsViewMode {
    LIST,
    CHART
}

data class AnalyticsState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val categories: List<Category> = emptyList(),
    val cards: List<Card> = emptyList(),
    val selectedCategories: List<Category> = emptyList(),
    val selectedCards: List<Card> = emptyList(),
    val selectedTimePeriod: TimePeriod = TimePeriod.NONE,
    val selectedSortOption: SortOption = SortOption.DATE_DESC,
    val selectedTransactionType: TransactionType? = null,
    val filteredTransactions: List<Transaction> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val monthlyComparisonLast6MonthConvertedTransactions: List<Transaction> = emptyList(),
    val viewMode: AnalyticsViewMode = AnalyticsViewMode.LIST,
    val selectedChart: ChartType = ChartType.LINE_CHART,
    val selectedCurrency: Currency = Currency.TRY

) : BaseState {
    override fun copyWithLoading(isLoading: Boolean): BaseState {
        return this.copy(isLoading = isLoading)
    }
}