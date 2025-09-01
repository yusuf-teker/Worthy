package com.yusufteker.worthy.screen.analytics.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.getCurrentEpochMillis
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.distinctCategoryIds
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.core.presentation.theme.Constants.ONE_DAY_MILLIS
import com.yusufteker.worthy.screen.analytics.domain.model.TimePeriod
import com.yusufteker.worthy.screen.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
                analyticsRepository.getCards(),
                analyticsRepository.getUserPrefCurrency()
            ) { transactions, cards, currency ->

                val distinctCategoryIds = transactions.distinctCategoryIds()

                // repository’den tüm kategorileri alıp filtrele
                val categories = analyticsRepository.getCategories()
                    .first() // Flow ise ilk değeri almak için
                    .filter { it.id in distinctCategoryIds }

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

                applyFilters()
            }.launchIn(viewModelScope)
        }

    }

    init {
        observeData()
    }

    private fun applyFilters() {
        val s = state.value
        val currentTime = getCurrentEpochMillis()

        val filtered = s.transactions
            .filter { tx ->
                if (s.selectedTimePeriod == TimePeriod.NONE) true
                else tx.transactionDate >= currentTime - (s.selectedTimePeriod.days * 24L * 60L * 60L * 1000L)
            }
            .filter { tx ->
                if (s.selectedCategories.isEmpty()) true
                else tx.categoryId in s.selectedCategories.map { it.id }
            }
            .filter { tx ->
                if (s.selectedCards.isEmpty()) true
                else tx.cardId in s.selectedCards.map { it.id }
            }

        _state.update { it.copy(filteredTransactions = filtered) }
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
                applyFilters()

            }

            is AnalyticsAction.OnCategorySelected -> {
                _state.update {
                    it.copy(
                        selectedCategories = if (action.isSelected) it.selectedCategories + action.category else it.selectedCategories - action.category
                    )
                }
                applyFilters()
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
                applyFilters()

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

        val convertedTransactions = state.value.transactions.map { tx ->
            tx.copy(
                amount = currencyConverter.convert(tx.amount, targetCurrency)
            )
        }

        _state.update {
            it.copy(transactions = convertedTransactions)
        }

    }

    suspend fun calculateMonthlyComparisonTransactions(
        currencyConverter: CurrencyConverter,
        targetCurrency: Currency
    ) {
        val currentTime = getCurrentEpochMillis()
        val periodStart =
            currentTime - (TimePeriod.SIX_MONTHS.days.toDouble() * ONE_DAY_MILLIS)

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
