package com.yusufteker.worthy.screen.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.yusufteker.worthy.core.presentation.theme.AppColors
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.DashboardOverviewCard
import com.yusufteker.worthy.core.presentation.components.AppButton
import com.yusufteker.worthy.core.presentation.theme.AppBrushes.screenBackground
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing16
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing8
import com.yusufteker.worthy.screen.dashboard.presentation.components.BottomSheetContent
import io.github.aakira.napier.Napier
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.dashboard_evaluate_purchase
import worthy.composeapp.generated.resources.dashboard_overview


@Composable
fun DashboardScreenRoot(
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (Routes) -> Unit,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is UiEvent.NavigateTo -> {
                    onNavigateTo (event.route)
                }

                else -> Unit
            }
        }
    }

    DashboardScreen(
        state = state,
        contentPadding = contentPadding,
        onAction = { action ->
            viewModel.onAction(action)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardState,
    contentPadding: PaddingValues = PaddingValues(),
    onAction: (DashboardAction) -> Unit,
) {

    BaseContentWrapper(
        state = state
    ){
        Column(Modifier.fillMaxSize()
            .background(
                //color = AppColors.background
                brush = screenBackground
            )
            .padding(contentPadding)
        )
        {

            AppTopBar(
                title = UiText.StringResourceId(Res.string.dashboard_overview).asString(),
                onNavIconClick = null,
                isAlignCenter = true,
                isBack = false,
                modifier = Modifier.background(AppColors.transparent)
            ) {  }



                Spacer(Modifier.height(Spacing8))
                // 3 – Kart
                DashboardOverviewCard(
                    modifier = Modifier.fillMaxWidth(),
                    amountText = state.totalAllIncomeMoney.formatted(),// todo + income eklenecek
                    incomeChangeRatio = state.incomeChangeRatio,
                    barsFractions = listOf(
                        state.fixedExpenseFraction,
                        state.desiresSpentFraction,
                        state.remainingFraction,
                        state.expensesFraction
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
                        modifier = Modifier
                    )
                }
                Spacer(Modifier.height(Spacing16))



        }
    }





    LaunchedEffect(state.isBottomSheetOpen){
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
            contentWindowInsets = {  WindowInsets.ime}
        ) {
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
