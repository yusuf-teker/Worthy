package com.yusufteker.worthy.screen.analytics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.NavigationModel
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.EmptyScreen
import com.yusufteker.worthy.core.presentation.components.SwipeToDeleteWrapper
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.Constants.EMPTY_SCREEN_SIZE
import com.yusufteker.worthy.screen.analytics.domain.model.TimePeriod
import com.yusufteker.worthy.screen.analytics.presentation.AnalyticsAction.OnItemDelete
import com.yusufteker.worthy.screen.analytics.presentation.components.BarChart
import com.yusufteker.worthy.screen.analytics.presentation.components.CategoryAnalysisPager
import com.yusufteker.worthy.screen.analytics.presentation.components.ChartType
import com.yusufteker.worthy.screen.analytics.presentation.components.ChartTypeSelector
import com.yusufteker.worthy.screen.analytics.presentation.components.LineChart
import com.yusufteker.worthy.screen.analytics.presentation.components.MonthlyComparisonCard
import com.yusufteker.worthy.screen.analytics.presentation.components.PieChartPager
import com.yusufteker.worthy.screen.analytics.presentation.components.SummaryCards
import com.yusufteker.worthy.screen.analytics.presentation.components.TopTransactionsCard
import com.yusufteker.worthy.screen.analytics.presentation.components.TransactionFilter
import com.yusufteker.worthy.screen.analytics.presentation.components.TransactionListItem
import com.yusufteker.worthy.screen.analytics.presentation.components.TrendAnalysisCard
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.analytics_empty_screen_button
import worthy.composeapp.generated.resources.chart_view
import worthy.composeapp.generated.resources.expenses
import worthy.composeapp.generated.resources.filter
import worthy.composeapp.generated.resources.incomes
import worthy.composeapp.generated.resources.list_view
import worthy.composeapp.generated.resources.screen_name_analytics
import worthy.composeapp.generated.resources.transaction

@Composable
fun AnalyticsScreenRoot(
    viewModel: AnalyticsViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (model: NavigationModel) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationHandler(viewModel) { navigationModel ->
        onNavigateTo(navigationModel)
    }

    BaseContentWrapper(state = state) {
        AnalyticsScreen(
            state = state, onAction = viewModel::onAction, contentPadding = contentPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    state: AnalyticsState,
    onAction: (action: AnalyticsAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {

    var showFilter by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.fillMaxSize().padding(contentPadding), topBar = {
            AppTopBar(
                title = UiText.StringResourceId(Res.string.screen_name_analytics).asString(),
                onNavIconClick = {
                    onAction(AnalyticsAction.NavigateBack)
                },
                actions = {
                    IconButton(
                        onClick = {
                            showFilter = !showFilter
                        }) {
                        Icon(
                            painter = painterResource(resource = Res.drawable.filter),
                            contentDescription = "filter",
                        )
                    }
                    IconButton(onClick = {
                        val newMode =
                            if (state.viewMode == AnalyticsViewMode.LIST) AnalyticsViewMode.CHART
                            else AnalyticsViewMode.LIST
                        onAction(AnalyticsAction.OnChangeViewMode(newMode))
                    }) {
                        Icon(
                            painter = painterResource(
                                resource = if (state.viewMode == AnalyticsViewMode.CHART) Res.drawable.chart_view
                                else Res.drawable.list_view
                            ), contentDescription = "switch view"
                        )
                    }
                })
        }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (state.transactions.isEmpty()) {
                EmptyScreen(
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.transaction),
                            contentDescription = "Add Transaction",
                            modifier = Modifier.size(EMPTY_SCREEN_SIZE)
                        )
                    },
                    buttonText = UiText.StringResourceId(Res.string.analytics_empty_screen_button)
                        .asString(),
                    onButtonClick = {
                        onAction(AnalyticsAction.OnAddTransactionClicked)
                    })
            } else {
                // TARİH FİLTRESİ // TODO FİLTER KISMINA ALINACAK
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    //  contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(TimePeriod.entries.toTypedArray()) { period ->
                        FilterChip(
                            modifier = Modifier.padding(end = 8.dp),
                            onClick = {
                                onAction(AnalyticsAction.OnPeriodSelected(period))
                            },
                            label = { Text(period.label.asString()) },
                            selected = state.selectedTimePeriod == period,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AppColors.onPrimary,
                                selectedLabelColor = AppColors.primary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                selectedBorderColor = AppColors.onPrimary,
                                selectedBorderWidth = 1.dp,
                                enabled = true,
                                selected = state.selectedTimePeriod == period
                            )
                        )
                    }
                }

                when (state.viewMode) {
                    AnalyticsViewMode.LIST -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.filteredTransactions, key = { it.id }) { transaction ->
                                SwipeToDeleteWrapper(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = CardDefaults.shape,
                                    onDelete = {
                                        onAction(OnItemDelete(transaction.id))
                                    },
                                ) {
                                    TransactionListItem(transaction = transaction)
                                }
                            }
                        }
                    }

                    AnalyticsViewMode.CHART -> {

                        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {

                            ChartTypeSelector(
                                selectedChart = state.selectedChart,
                                onChartChange = { onAction(AnalyticsAction.OnChangeChartType(it)) })
                            Spacer(modifier = Modifier.height(16.dp))


                            Card(
                                modifier = Modifier.fillMaxWidth().height(400.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = AppColors.surfaceVariant
                                )
                            ) {
                                when (state.selectedChart) {
                                    ChartType.LINE_CHART -> LineChart(
                                        transactions = state.filteredTransactions,
                                        selectedPeriod = state.selectedTimePeriod
                                    )

                                    ChartType.PIE_CHART -> {

                                        PieChartPager(
                                            transactionsList = listOf(
                                                state.filteredTransactions.filter { it.transactionType == TransactionType.EXPENSE },
                                                state.filteredTransactions.filter { it.transactionType == TransactionType.INCOME }),
                                            labels = listOf(
                                                UiText.StringResourceId(Res.string.expenses)
                                                    .asString(),
                                                UiText.StringResourceId(Res.string.incomes)
                                                    .asString()
                                            ),
                                            categories = state.categories,
                                            backgroundColor = AppColors.secondaryContainer
                                        )
                                    }

                                    ChartType.BAR_CHART -> {
                                        BarChart(
                                            state.filteredTransactions,
                                            state.selectedTimePeriod
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            SummaryCards(
                                transactions = state.filteredTransactions,
                                selectedPeriod = state.selectedTimePeriod
                            )

                            Spacer(modifier = Modifier.height(16.dp))


                            CategoryAnalysisPager(state.filteredTransactions, state.categories)

                            Spacer(modifier = Modifier.height(16.dp))

                            TrendAnalysisCard(state.filteredTransactions, state.selectedTimePeriod)

                            Spacer(modifier = Modifier.height(16.dp))
                            // SON 6 AYIN CONVERTED EDILMIS İŞLEMLERİ
                            MonthlyComparisonCard(state.monthlyComparisonLast6MonthConvertedTransactions)

                            Spacer(modifier = Modifier.height(16.dp))

                            TopTransactionsCard(state.filteredTransactions)

                        }

                    }
                }
            }

        }

        if (showFilter) {
            ModalBottomSheet(
                onDismissRequest = { showFilter = false },
            ) {
                TransactionFilter(
                    categories = state.categories,
                    cards = state.cards,
                    selectedCategories = state.selectedCategories,
                    selectedCards = state.selectedCards,
                    selectedTimePeriod = state.selectedTimePeriod,
                    onSelectedTimePeriodSelected = { onAction(AnalyticsAction.OnPeriodSelected(it)) },
                    onCategorySelected = { category, isSelected ->
                        onAction(AnalyticsAction.OnCategorySelected(category, isSelected))

                    },
                    onCardSelected = { card, isSelected ->
                        onAction(AnalyticsAction.OnCardSelected(card, isSelected))
                    },
                    clearFilters = {
                        onAction(AnalyticsAction.ClearFilters)
                    })
            }
        }
    }

}