package com.yusufteker.worthy.screen.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.app.navigation.NavigationModel
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.AppScaffold
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppButton
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing16
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing8
import com.yusufteker.worthy.core.presentation.util.formatted
import com.yusufteker.worthy.core.presentation.util.formattedShort
import com.yusufteker.worthy.screen.dashboard.presentation.components.BottomSheetContent
import com.yusufteker.worthy.screen.dashboard.presentation.components.DashboardOverviewCard
import io.github.aakira.napier.Napier
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.dashboard_evaluate_purchase
import worthy.composeapp.generated.resources.dashboard_overview

@Composable
fun DashboardScreenRoot(
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (NavigationModel) -> Unit,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateTo -> {
                    onNavigateTo(event.toModel())
                }

                else -> Unit
            }
        }
    }
    BaseContentWrapper(
        state = state
    ) { modifier ->
        DashboardScreen(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            onAction = { action ->
                viewModel.onAction(action)
            },
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    state: DashboardState,
    contentPadding: PaddingValues = PaddingValues(),
    onAction: (DashboardAction) -> Unit,
) {

    AppScaffold(
        modifier = modifier.padding(contentPadding),
        topBar = {
            AppTopBar(
                title = UiText.StringResourceId(Res.string.dashboard_overview).asString(),
                onNavIconClick = null,
                isBack = false,
                showDivider = false,
                modifier = Modifier.background(AppColors.transparent)
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues).padding(top = 8.dp)) {

            DashboardOverviewCard(
                modifier = Modifier.fillMaxWidth(),
                isLoading = state.isLoading,
                amountText = state.totalAllIncomeMoney.formattedShort(),// todo + income eklenecek
                incomeChangeRatio = state.incomeChangeRatio,
                barsFractions = listOf(
                    state.fixedExpenseFraction,
                    state.desiresSpentFraction,
                    state.remainingFraction,
                    state.expensesFraction
                ),
                barsAmount = listOf(
                    state.fixedExpenseMoney.formattedShort(),
                    state.desiresSpentMoney.formattedShort(),
                    state.remainingMoney.formattedShort(),
                    state.expensesMoney.formattedShort()

                ),
                miniBarsFractions = state.selectedMiniBarsFraction,
                miniBarsMonths = state.selectedMiniBarsMonths,

                selectedChartIndex = state.selectedChartIndex,
                onChartSelected = {
                    onAction(DashboardAction.ChartSelected(it))
                },
                selectableMonths = state.selectableMonths,
                selectedMonth = state.selectedMonthYear,
                onSelectedMonthChanged = { yearMonth ->
                    onAction(DashboardAction.OnSelectedMonthChanged(yearMonth))
                },
                onAddWishlistClicked = { onAction(DashboardAction.AddWishlistClicked) },
                onAddRecurringClicked = { onAction(DashboardAction.AddRecurringClicked) },
                onAddTransactionClicked = { onAction(DashboardAction.AddTransactionClicked) }

            )

            Spacer(Modifier.weight(1f))

            // 4 – Evaluate Purchase
            Row {
                Spacer(modifier = Modifier.weight(1f))
                AppButton(
                    text = UiText.StringResourceId(Res.string.dashboard_evaluate_purchase).asString(),
                    onClick = { onAction(DashboardAction.EvaluateButtonClicked) },
                    textModifier = Modifier.widthIn(max = 85.dp),

                    )
            }
            Spacer(Modifier.height(Spacing16))

        }

    }





    LaunchedEffect(state.isBottomSheetOpen) {
        Napier.d("bottom sheet is open ${state.isBottomSheetOpen}")
    }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    if (state.isBottomSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { onAction(DashboardAction.CloseBottomSheetClicked) },
            sheetState = bottomSheetState,
            containerColor = AppColors.surface,
            tonalElevation = 6.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            contentWindowInsets = { WindowInsets.ime }) {
            BottomSheetContent(
                sheetState = bottomSheetState,
                onClose = {
                    onAction(DashboardAction.CloseBottomSheetClicked)
                },
                onCalculate = { amount ->
                    onAction(
                        DashboardAction.CalculateButtonClicked(money = amount)
                    )
                },
                onPurchase = { expense ->
                    onAction(
                        DashboardAction.PurchaseButtonClicked(expense)
                    )
                },
                bottomSheetUiState = state.bottomSheetUiState,

                )
        }
    }

}
