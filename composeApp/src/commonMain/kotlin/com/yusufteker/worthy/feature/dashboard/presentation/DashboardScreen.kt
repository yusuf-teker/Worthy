package com.yusufteker.worthy.feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.LineHeightStyle
import com.yusufteker.worthy.core.domain.MonthlyAmount
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.IncomeAllocationCard
import com.yusufteker.worthy.core.presentation.components.LabelledProgressBar
import com.yusufteker.worthy.core.presentation.components.PrimaryButton
import com.yusufteker.worthy.core.presentation.theme.AppBrushes.screenBackground
import com.yusufteker.worthy.core.presentation.theme.AppDimens.ScreenPadding
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing16
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing24
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing32
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing8
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.dashboard_desires_budget
import worthy.composeapp.generated.resources.dashboard_evaluate_purchase
import worthy.composeapp.generated.resources.dashboard_monthly_income
import worthy.composeapp.generated.resources.dashboard_overview
import worthy.composeapp.generated.resources.dashboard_savings_goal



@Composable
fun DashboardScreenRoot(
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateToEvaluation: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    DashboardScreen(
        state = state,
        contentPadding = contentPadding,
        onAction = { action ->
            viewModel.onAction(action)
            if (action == DashboardAction.FabClicked) onNavigateToEvaluation()
        },
        onWishlistClick = onNavigateToWishlist
    )
}

@Composable
fun DashboardScreen(
    state: DashboardState,
    contentPadding: PaddingValues = PaddingValues(),
    onAction: (DashboardAction) -> Unit,
    onWishlistClick: () -> Unit
) {



        Column(Modifier.fillMaxSize()
            .background(
                //color = AppColors.background
                brush = screenBackground
            )
            .padding(contentPadding)
        ) {

            AppTopBar(
                title = UiText.StringResourceId(Res.string.dashboard_overview).asString(),
                onNavIconClick = null,
                isAlignCenter = true,
                isBack = false,
                modifier = Modifier.background(AppColors.transparent)
            ) {  }

            if (state.isLoading){
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }else{
                Spacer(Modifier.height(Spacing8))

                /*
                           // 1 – Progress barlar
                           LabelledProgressBar(
                               label = UiText.StringResourceId(Res.string.dashboard_savings_goal).asString(),
                               progress = state.savingProgress,
                               trailingText = "600"
                           )
                           Spacer(Modifier.height(Spacing24))
                           LabelledProgressBar(
                               label =UiText.StringResourceId(Res.string.dashboard_desires_budget).asString(),
                               progress = state.desiresSpentFraction,
                               trailingText = "750"
                           )
                           Spacer(Modifier.height(Spacing32))

                           // 2 – Monthly Income başlık
                           Text(
                               text = UiText.StringResourceId(Res.string.dashboard_monthly_income).asString(),
                               style = AppTypography.titleLarge,
                               color = AppColors.onBackground
                           )
                           Spacer(Modifier.height(Spacing16))
                           */
                // 3 – Kart
                IncomeAllocationCard(
                    modifier = Modifier.fillMaxWidth(),
                    amountText = "₺${state.monthlyIncome.toInt()}",
                    monthDeltaText = "+10%",
                    bars = listOf(
                        state.fixedExpenseFraction,
                        state.desiresSpentFraction,
                        state.expensesFraction,
                        1f - state.fixedExpenseFraction - state.desiresSpentFraction
                    ),
                    selectedChartIndex = state.selectedChartIndex,
                    onChartSelected = {
                        onAction(DashboardAction.ChartSelected(it))
                    },
                    last6MonthAmounts = state.last6MonthAmounts

                )

                Spacer(Modifier.weight(1f))

                // 4 – Evaluate Purchase
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    PrimaryButton(
                        text = UiText.StringResourceId(Res.string.dashboard_evaluate_purchase).asString(),
                        onClick = { onAction(DashboardAction.FabClicked) },
                        modifier = Modifier
                    )
                }
                Spacer(Modifier.height(Spacing16))

            }

        }


}
