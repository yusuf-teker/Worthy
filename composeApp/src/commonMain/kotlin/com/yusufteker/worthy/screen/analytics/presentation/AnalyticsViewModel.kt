package com.yusufteker.worthy.screen.analytics.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.getCurrentEpochMillis
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.analytics.domain.repository.AnalyticsRepository
import com.yusufteker.worthy.screen.analytics.domain.model.TimePeriod
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

class AnalyticsViewModel(
    private val analyticsRepository: AnalyticsRepository,
    private val currencyConverter: CurrencyConverter
) : BaseViewModel<AnalyticsState>(AnalyticsState()) {

    fun observeData() {
        launchWithLoading {
            combine(
                analyticsRepository.getTransactions(),
                analyticsRepository.getCategories(),
                analyticsRepository.getCards(),
                analyticsRepository.getUserPrefCurrency()
            ) { transactions, categories, cards, currency ->
                _state.update {
                    it.copy(
                        transactions = transactions,
                        categories = categories,
                        cards = cards,
                        selectedCurrency = currency

                    )
                }
                launchWithLoading {
                    filterTransactionsWithCurrencyConverter(
                        currencyConverter,
                        state.value.selectedCurrency
                    )
                    calculateMonthlyComparisonTransactions(
                        currencyConverter,
                        state.value.selectedCurrency
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

            is AnalyticsAction.ClearFilters -> {
                _state.update {
                    it.copy(
                        selectedCategories = emptyList(),
                        selectedCards = emptyList(),
                        selectedTimePeriod = TimePeriod.NONE
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

            is AnalyticsAction.OnItemDelete -> {
                launchWithLoading {
                    analyticsRepository.deleteTransaction(action.id)
                }
            }

            is AnalyticsAction.NavigateBack -> {
                navigateBack()
            }

            is AnalyticsAction.OnChangeViewMode -> {

                _state.update {
                    it.copy(
                        viewMode = action.viewMode
                    )
                }
            }

            is AnalyticsAction.OnPeriodSelected -> {

                _state.update {
                    it.copy(
                        selectedTimePeriod = action.period,
                    )
                }
                launchWithLoading {
                    filterTransactionsWithCurrencyConverter(
                        currencyConverter,
                        state.value.selectedCurrency
                    )
                }

            }

            is AnalyticsAction.OnChangeChartType -> {
                _state.update {
                    it.copy(
                        selectedChart = action.chartType
                    )
                }
            }

            is AnalyticsAction.OnAddTransactionClicked -> {
                navigateTo(Routes.AddTransaction())
            }
        }
    }

    suspend fun filterTransactionsWithCurrencyConverter(
        currencyConverter: CurrencyConverter,
        targetCurrency: Currency
    ) {
        val currentTime = getCurrentEpochMillis()
        val periodStart =
            currentTime - (state.value.selectedTimePeriod.days.toDouble() * 24 * 60 * 60 * 1000)

        val filtered = if (state.value.selectedTimePeriod == TimePeriod.NONE) {
            state.value.transactions
        } else {
            state.value.transactions.filter { it.transactionDate >= periodStart }
        }

        val convertedTransactions = filtered.map { tx ->
            tx.copy(
                amount = currencyConverter.convert(tx.amount, targetCurrency)
            )
        }

        _state.update {
            it.copy(filteredTransactions = convertedTransactions)
        }
    }

    suspend fun calculateMonthlyComparisonTransactions(
        currencyConverter: CurrencyConverter,
        targetCurrency: Currency
    ) {
        val currentTime = getCurrentEpochMillis()
        val periodStart =
            currentTime - (TimePeriod.SIX_MONTHS.days.toDouble() * 24 * 60 * 60 * 1000)

        val filtered = state.value.transactions.filter { it.transactionDate >= periodStart }

        val convertedTransactions = filtered.map { tx ->
            tx.copy(
                amount = currencyConverter.convert(tx.amount, targetCurrency)
            )
        }

        _state.update {
            it.copy(monthlyComparisonLast6MonthConvertedTransactions = convertedTransactions)
        }
    }

}
