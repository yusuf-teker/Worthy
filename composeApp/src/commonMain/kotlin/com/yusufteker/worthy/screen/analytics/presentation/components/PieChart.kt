package com.yusufteker.worthy.screen.analytics.presentation.components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.getColorByIndex
import com.yusufteker.worthy.core.domain.model.getNameResource
import com.yusufteker.worthy.core.presentation.formatTwoDecimals
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.toRadians
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(
    transactions: List<Transaction>,
    categories: List<Category>,
    backgroundColor: Color = AppColors.secondaryContainer,
    label: String = ""
) {
    var animationProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(transactions) {
        animate(0f, 1f, animationSpec = tween(2000)) { value, _ ->
            animationProgress = value
        }
    }

    val currency = transactions.firstOrNull()?.amount?.currency ?: Currency.TRY
    val textMeasurer = rememberTextMeasurer()

    val categoryTotals = categories.map { category ->
        val total = transactions.filter { it.categoryId == category.id }.sumOf { it.amount.amount }
        category to total
    }.filter { it.second > 0 }

    val totalAmount = categoryTotals.sumOf { it.second }

    if (categoryTotals.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().background(backgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (label.isNotEmpty()) {
                Text(
                    text = label,
                    style = AppTypography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Canvas(
                modifier = Modifier.fillMaxSize(0.7f)

            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = kotlin.math.min(size.width, size.height) * 0.3f

                var currentAngle = -90f

                categoryTotals.forEachIndexed { i, (category, amount) ->
                    val startAngle = currentAngle
                    val sweepAngle = (amount / totalAmount * 360f).toFloat() * animationProgress
                    // dilimi çiz
                    drawArc(
                        color = getColorByIndex(i),
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2)
                    )

                    // ikon yerleşimi
                    if (category.icon != null) {
                        val middleAngle = startAngle + sweepAngle / 2f
                        val rad = middleAngle.toDouble().toRadians()

                        // merkezin biraz dışında (ör: radius * 1.2f)
                        val iconRadius = radius * 1.2f
                        val iconX = center.x + cos(rad).toFloat() * iconRadius
                        val iconY = center.y + sin(rad).toFloat() * iconRadius

                        drawContext.canvas.nativeCanvas.apply {
                            val layoutResult = textMeasurer.measure(category.icon)
                            drawText(
                                layoutResult, topLeft = Offset(
                                    iconX - layoutResult.size.width / 2,
                                    iconY - layoutResult.size.height / 2
                                )
                            )
                        }
                    }

                    currentAngle += sweepAngle
                }

                // Draw center circle for donut effect
                drawCircle(
                    color = backgroundColor, radius = radius * 0.5f, center = center
                )
            }

            // Legend
            FlowRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoryTotals.forEachIndexed { i, (category, amount) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(16.dp)
                                .background(getColorByIndex(i), CircleShape)
                        )
                        Column {
                            Text(
                                text = category.getNameResource(), style = AppTypography.labelSmall
                            )
                            Text(
                                text = "${amount.formatTwoDecimals()} ${currency.symbol}",
                                style = AppTypography.labelSmall.copy(
                                    color = AppColors.onSurface.copy(alpha = 0.7f)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PieChartPager(
    transactionsList: List<List<Transaction>>,
    categories: List<Category>,
    backgroundColor: Color = AppColors.secondaryContainer,
    labels: List<String> = emptyList()
) {
    val filteredList = transactionsList.filter { it.isNotEmpty() }

    val pagerState = rememberPagerState(pageCount = { filteredList.size })


    Column(
        modifier = Modifier.fillMaxSize().background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxWidth().weight(1f)
        ) { page ->
            // Her sayfada bir PieChart
            PieChart(
                transactions = filteredList[page],
                categories = categories,
                backgroundColor = backgroundColor,
                label = labels.getOrNull(page) ?: ""
            )
        }

        // Indicator
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            repeat(filteredList.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier.size(if (isSelected) 12.dp else 8.dp).clip(CircleShape)
                        .background(
                            if (isSelected) AppColors.primary
                            else AppColors.onSurface.copy(alpha = 0.3f)
                        ).padding(4.dp)
                )
                if (index != transactionsList.lastIndex) Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

