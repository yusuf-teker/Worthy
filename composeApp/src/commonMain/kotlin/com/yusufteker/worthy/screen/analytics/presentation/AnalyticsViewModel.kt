package com.yusufteker.worthy.screen.analytics.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.analytics.domain.AnalyticsRepository
import com.yusufteker.worthy.screen.analytics.presentation.components.DateRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

class AnalyticsViewModel(
    private val analyticsRepository: AnalyticsRepository
) : BaseViewModel<AnalyticsState>(AnalyticsState()) {


    fun observeData(){
        launchWithLoading {
            combine(
                analyticsRepository.getTransactions(),
                analyticsRepository.getCategories(),
                analyticsRepository.getCards()
            ) { transactions, categories, cards ->
                _state.update {
                    it.copy(
                        transactions = transactions,
                        categories = categories,
                        cards = cards
                    )
                }

            }.launchIn(viewModelScope)
        }

    }

    init {
        observeData()
    }
    fun onAction(action: AnalyticsAction) {
        when (action) {
            is AnalyticsAction.Init -> {
                // TODO
            }

            AnalyticsAction.ClearFilters -> {
                _state.update {
                    it.copy(
                        selectedCategories = emptyList(),
                        selectedCards = emptyList(),
                        selectedDateRange = DateRange.ALL_TIME
                    )
                }
            }
            is AnalyticsAction.OnCardSelected -> {
                _state.update {
                    it.copy(
                        selectedCards = if (action.isSelected) it.selectedCards + action.card else it.selectedCards - action.card
                    )
                }
            }
            is AnalyticsAction.OnCategorySelected -> {
                _state.update {
                    it.copy(
                        selectedCategories = if (action.isSelected) it.selectedCategories + action.category else it.selectedCategories - action.category
                    )
                }
            }
            is AnalyticsAction.OnDateRangeSelected -> {
                _state.update {
                    it.copy(
                        selectedDateRange = action.dateRange
                    )
                }
            }
            is AnalyticsAction.OnItemDelete -> {
                launchWithLoading {
                    analyticsRepository.deleteTransaction(action.id)
                }
            }

            AnalyticsAction.NavigateBack -> {
                navigateBack()
            }
        }
    }
}