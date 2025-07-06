package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.MonthlyAmount
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppDimens.AppIconSizeSmall
import com.yusufteker.worthy.core.presentation.theme.AppDimens.PaddingL
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing16
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import io.github.aakira.napier.Napier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.chart_desires
import worthy.composeapp.generated.resources.chart_expenses
import worthy.composeapp.generated.resources.chart_fixed_expenses
import worthy.composeapp.generated.resources.chart_remaining
import worthy.composeapp.generated.resources.history
import worthy.composeapp.generated.resources.income_allocation_this_month
import worthy.composeapp.generated.resources.income_allocation_title

@Composable
fun IncomeAllocationCard(
    amountText: String = "$5,000",
    monthDeltaText: String = "+10%",
    bars: List<Float>,
    modifier: Modifier = Modifier,
    colors : CardColors = CardDefaults.cardColors(
        containerColor = AppColors.transparent
    ),
    selectedChartIndex: Int? = null,
    onChartSelected: (index: Int) -> Unit,
    last6MonthAmounts: List<MonthlyAmount>

) {

    LaunchedEffect(selectedChartIndex){
        Napier.d("IncomeAllocationCard Chart selected: $selectedChartIndex")
    }
    Card(
        modifier = modifier,
        colors = colors,
        border = BorderStroke(1.dp,AppColors.surfaceVariant)
    ) {
        Column(Modifier.padding(24.dp)) {
            Text(
                text = UiText.StringResourceId(Res.string.income_allocation_title).asString(),
                style = AppTypography.titleMedium,
                color = AppColors.onSurface
            )
            Spacer(Modifier.height(16.dp))
            Text(
                amountText,
                style = AppTypography.displaySmall,
                color = AppColors.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = UiText.StringResourceId(Res.string.income_allocation_this_month,arrayOf(monthDeltaText)).asString(),
                style = AppTypography.bodyMedium,
                color = AppColors.tertiary   // yeşil/turuncu varyant
            )
            Spacer(Modifier.height(24.dp))

            ColumnBarChart(
                values = bars,
                labels = listOf(
                    UiText.StringResourceId(Res.string.chart_fixed_expenses).asString(),
                    UiText.StringResourceId(Res.string.chart_desires).asString(),
                    UiText.StringResourceId(Res.string.chart_remaining).asString(),
                    UiText.StringResourceId(Res.string.chart_expenses).asString()
                ),
                selectedIndex = selectedChartIndex,
                onBarClick = {
                    Napier.d("Chart selected: $it")
                    onChartSelected.invoke(it)
                }
            )

            // IncomeAllocationCard içinde
            AnimatedVisibility(
                visible = selectedChartIndex != null,
                enter = fadeIn() + expandVertically(
                    expandFrom = Alignment.Top,   // üstten açılma hissi
                    animationSpec = tween(300)
                ),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween(300)
                ) + fadeOut()
            ) {


            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = PaddingL)) {

                Icon(
                    painter = painterResource(resource = Res.drawable.history),
                    contentDescription = "gecmis",
                    tint = AppColors.onBackground,
                    modifier = Modifier
                        .size(AppIconSizeSmall)
                )
                Spacer(Modifier.width(Spacing16))
                MiniBarChart(last6MonthAmounts)

            }

            }

        }
    }
}

@Preview
@Composable
fun IncomeAllocationCardPreview(){
    Column(Modifier.background(AppColors.background)) {
        IncomeAllocationCard(
            bars = listOf(0.25f, 0.5f, 0.75f),
            selectedChartIndex = 1,
            onChartSelected = {},
            last6MonthAmounts = listOf(
                MonthlyAmount("OCAK", 0.4f),
                MonthlyAmount("SUBAT", 0.2f),
                MonthlyAmount("MART", 0.3f),
            )
        )
    }
}