package com.yusufteker.worthy.screen.transactions.list.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.chart_type_bar
import worthy.composeapp.generated.resources.chart_type_line
import worthy.composeapp.generated.resources.chart_type_pie

enum class ChartType(val label: UiText) {
    LINE_CHART(UiText.StringResourceId(Res.string.chart_type_line)),
    PIE_CHART(UiText.StringResourceId(Res.string.chart_type_pie)),
    BAR_CHART(UiText.StringResourceId(Res.string.chart_type_bar)),
}

@Composable
fun ChartTypeSelector(
    selectedChart: ChartType,
    onChartChange: (ChartType) -> Unit
) {
    val chartIcons = mapOf(
        ChartType.LINE_CHART to "📈",
        ChartType.PIE_CHART to "🥧",
        ChartType.BAR_CHART to "📊",

        )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp) // baş ve son boşluk

    ) {
        items(ChartType.values()) { chartType ->
            val isSelected = selectedChart == chartType
            val animatedScale by animateFloatAsState(
                targetValue = if (isSelected) 1.1f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )

            Card(
                modifier = Modifier
                    .scale(animatedScale)
                    .clickable { onChartChange(chartType) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected)
                        AppColors.primaryContainer
                    else AppColors.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isSelected) 8.dp else 2.dp
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chartIcons[chartType] ?: "📊",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = chartType.label.asString(),
                        style = AppTypography.labelMedium,
                        color = if (isSelected)
                            AppColors.onPrimaryContainer
                        else AppColors.onSurface
                    )
                }
            }
        }
    }
}
