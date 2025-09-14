package com.yusufteker.worthy.screen.transactions.list.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.AppScaffold
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.EmptyScreen
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.Constants.EMPTY_SCREEN_SIZE
import com.yusufteker.worthy.screen.transactions.list.presentation.components.BarChart
import com.yusufteker.worthy.screen.transactions.list.presentation.components.CategoryAnalysisPager
import com.yusufteker.worthy.screen.transactions.list.presentation.components.ChartType
import com.yusufteker.worthy.screen.transactions.list.presentation.components.ChartTypeSelector
import com.yusufteker.worthy.screen.transactions.list.presentation.components.LineChart
import com.yusufteker.worthy.screen.transactions.list.presentation.components.MonthlyComparisonCard
import com.yusufteker.worthy.screen.transactions.list.presentation.components.PieChartPager
import com.yusufteker.worthy.screen.transactions.list.presentation.components.ScrollAwareButtons
import com.yusufteker.worthy.screen.transactions.list.presentation.components.SummaryCards
import com.yusufteker.worthy.screen.transactions.list.presentation.components.TopTransactionsCard
import com.yusufteker.worthy.screen.transactions.list.presentation.components.TransactionFilter
import com.yusufteker.worthy.screen.transactions.list.presentation.components.TransactionListAccordion
import com.yusufteker.worthy.screen.transactions.list.presentation.components.TransactionSortSheet
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.analytics_empty_screen_button
import worthy.composeapp.generated.resources.chart_view
import worthy.composeapp.generated.resources.expenses
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

    BaseContentWrapper(state = state) { modifier ->
        AnalyticsScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    state: AnalyticsState,
    onAction: (action: AnalyticsAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier
) {

    var showFilter by remember { mutableStateOf(false) }
    var showSort by remember { mutableStateOf(false) }


    AppScaffold(
        modifier = modifier.padding(contentPadding), topBar = {
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = UiText.StringResourceId(Res.string.screen_name_analytics).asString(),
                actions = {
                    IconButton(
                        modifier = Modifier.padding(0.dp).size(36.dp),
                        onClick = {
                        val newMode =
                            if (state.viewMode == AnalyticsViewMode.LIST) AnalyticsViewMode.CHART
                            else AnalyticsViewMode.LIST
                        onAction(AnalyticsAction.OnChangeViewMode(newMode))
                    }) {
                        Icon(
                            painter = painterResource(
                                resource = if (state.viewMode == AnalyticsViewMode.CHART) Res.drawable.list_view
                                else Res.drawable.chart_view
                            ), contentDescription = "switch view"
                        )
                    }
                })
        }) { paddingValues ->
        Column(
            modifier = modifier.fillMaxWidth().padding(top = paddingValues.calculateTopPadding()),
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

                when (state.viewMode) {

                    AnalyticsViewMode.LIST -> {
                        val listState = rememberLazyListState()

                        ScrollAwareButtons(
                            onSortClick = { showSort = true },
                            onFilterClick = { showFilter = true },
                            lazyListState = listState
                        )

                        TransactionListAccordion(
                            transactions = state.filteredTransactions,
                            convertedTransactions = state.convertedTransactions,
                            listState = listState,
                            onDelete = {
                                //onAction(OnItemDelete(it)) TODO silmeyi kaldırdım şimdilik
                            },
                            onItemClicked = {
                                onAction(AnalyticsAction.OnTransactionClicked(it))
                            })

                    }

                    AnalyticsViewMode.CHART -> {
                        val scrollState = rememberScrollState()
                        ScrollAwareButtons(
                            onSortClick = { showSort = true },
                            onFilterClick = { showFilter = true },
                            scrollState = scrollState
                        )
                        Column(Modifier.fillMaxWidth().verticalScroll(scrollState)) {

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
                                        transactions = state.convertedFilteredTransactions,
                                        selectedPeriod = state.selectedTimePeriod
                                    )

                                    ChartType.PIE_CHART -> {

                                        PieChartPager(
                                            transactionsList = listOf(
                                                state.convertedFilteredTransactions.filter { it.transactionType == TransactionType.EXPENSE },
                                                state.convertedFilteredTransactions.filter { it.transactionType == TransactionType.INCOME }),
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
                                            state.convertedFilteredTransactions,
                                            state.selectedTimePeriod
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            SummaryCards(
                                transactions = state.convertedFilteredTransactions,
                                selectedPeriod = state.selectedTimePeriod
                            )

                            Spacer(modifier = Modifier.height(16.dp))


                            CategoryAnalysisPager(
                                state.convertedFilteredTransactions, state.categories
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Spacer(modifier = Modifier.height(16.dp))
                            // SON 6 AYIN CONVERTED EDILMIS İŞLEMLERİ
                            MonthlyComparisonCard(state.monthlyComparisonLast6MonthConvertedTransactions)

                            Spacer(modifier = Modifier.height(16.dp))

                            TopTransactionsCard(state.convertedFilteredTransactions)

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
                    selectedTransactionType = state.selectedTransactionType,
                    onSelectedTimePeriodSelected = { onAction(AnalyticsAction.OnPeriodSelected(it)) },
                    onCategorySelected = { category, isSelected ->
                        onAction(AnalyticsAction.OnCategorySelected(category, isSelected))

                    },
                    onCardSelected = { card, isSelected ->
                        onAction(AnalyticsAction.OnCardSelected(card, isSelected))
                    },
                    onTransactionTypeSelected = {
                        onAction(AnalyticsAction.OnTransactionTypeSelected(it))
                    },
                    clearFilters = {
                        onAction(AnalyticsAction.ClearFilters)
                    })
            }
        }

        if (showSort) {
            TransactionSortSheet(
                selectedSort = state.selectedSortOption,
                onSortSelected = { sortOption ->
                    onAction(AnalyticsAction.OnSortSelected(sortOption))
                    showSort = false
                },
                onDismiss = { showSort = false })
        }
    }

}

