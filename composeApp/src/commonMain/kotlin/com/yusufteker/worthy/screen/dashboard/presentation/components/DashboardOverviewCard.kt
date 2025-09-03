package com.yusufteker.worthy.screen.dashboard.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.ColumnBarChart
import com.yusufteker.worthy.core.presentation.components.MiniBarChart
import com.yusufteker.worthy.core.presentation.formatPercentageChange
import com.yusufteker.worthy.core.presentation.getMonthName
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
import worthy.composeapp.generated.resources.income_allocation_compare_to_last_month
import worthy.composeapp.generated.resources.income_allocation_title

@Composable
fun DashboardOverviewCard(
    amountText: String = "",
    incomeChangeRatio: Double = 0.0,
    barsFractions: List<Float>,
    barsAmount: List<String>,
    miniBarsFractions: List<Float?>,
    miniBarsMonths: List<Int>,
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = AppColors.transparent
    ),
    selectedChartIndex: Int? = null,
    onChartSelected: (index: Int) -> Unit,
    selectableMonths: List<AppDate>,
    selectedMonth: AppDate,
    onSelectedMonthChanged: (AppDate) -> Unit,
    onAddWishlistClicked: () -> Unit = {},
    onAddRecurringClicked: () -> Unit = {},
    onAddTransactionClicked: () -> Unit = {}

) {

    LaunchedEffect(selectedChartIndex) {
        Napier.d("IncomeAllocationCard Chart selected: $selectedChartIndex")
    }
    Card(
        modifier = modifier, colors = colors, border = BorderStroke(1.dp, AppColors.surfaceVariant)
    ) {
        Column(Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = UiText.StringResourceId(Res.string.income_allocation_title).asString(),
                    style = AppTypography.titleMedium,
                    color = AppColors.onSurface,
                    modifier = Modifier.weight(1f)
                )

                var expanded by remember { mutableStateOf(false) }

                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(getMonthName(selectedMonth.month).asString())
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = expanded, onDismissRequest = { expanded = false }) {
                        selectableMonths.forEach { monthYear ->

                            DropdownMenuItem(
                                text = { Text(getMonthName(monthYear.month).asString()) },
                                onClick = {
                                    onSelectedMonthChanged(monthYear)
                                    expanded = false
                                })
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                amountText, style = AppTypography.displaySmall, color = AppColors.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = UiText.StringResourceId(
                    Res.string.income_allocation_compare_to_last_month,
                    arrayOf(formatPercentageChange(incomeChangeRatio))
                ).asString(),
                style = AppTypography.bodyMedium,
                color = if (incomeChangeRatio > 0) AppColors.savingsGreen else if (incomeChangeRatio == 0.0) AppColors.onSurface.copy(alpha = 0.4f) else AppColors.error
            )
            Spacer(Modifier.height(24.dp))


            ColumnBarChart(
                values = barsFractions,
                labels = listOf(
                    UiText.StringResourceId(Res.string.chart_fixed_expenses).asString(),
                    UiText.StringResourceId(Res.string.chart_desires).asString(),
                    UiText.StringResourceId(Res.string.chart_remaining).asString(),
                    UiText.StringResourceId(Res.string.chart_expenses).asString()
                ),
                amounts = barsAmount,
                selectedIndex = selectedChartIndex,
                onBarClick = {
                    Napier.d("Chart selected: $it")
                    onChartSelected.invoke(it)
                },
                onAddWishlistClicked = { onAddWishlistClicked.invoke() },
                onAddRecurringClicked = { onAddRecurringClicked.invoke() },
                onAddTransactionClicked = { onAddTransactionClicked.invoke() })

            // IncomeAllocationCard içinde
            AnimatedVisibility(
                visible = selectedChartIndex != null, enter = fadeIn() + expandVertically(
                    expandFrom = Alignment.Top,   // üstten açılma hissi
                    animationSpec = tween(300)
                ), exit = shrinkVertically(
                    shrinkTowards = Alignment.Top, animationSpec = tween(300)
                ) + fadeOut()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = PaddingL)
                ) {

                    Icon(
                        painter = painterResource(resource = Res.drawable.history),
                        contentDescription = "gecmis",
                        tint = AppColors.onBackground,
                        modifier = Modifier.size(AppIconSizeSmall)
                    )
                    Spacer(Modifier.width(Spacing16))
                    MiniBarChart(
                        values = miniBarsFractions, labels = miniBarsMonths
                    )

                }

            }

        }
    }
}

@Preview
@Composable
fun IncomeAllocationCardPreview() {
    Column(Modifier.background(AppColors.background)) {
        DashboardOverviewCard(
            barsFractions = listOf(0.25f, 0.5f, 0.75f),
            selectedChartIndex = 1,
            onChartSelected = {},
            onSelectedMonthChanged = {},
            selectableMonths = listOf(
                AppDate(2023, 1),
                AppDate(2023, 2),
                AppDate(2023, 3),
                AppDate(2023, 4),
                AppDate(2023, 5),
                AppDate(2023, 6)

            ),
            selectedMonth = AppDate(2023, 1),
            miniBarsFractions = emptyList(),
            barsAmount = emptyList(),
            miniBarsMonths = emptyList()
        )
    }
}