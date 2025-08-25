package com.yusufteker.worthy.screen.analytics.presentation.components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.getCurrentEpochMillis
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.screen.analytics.domain.model.TimePeriod
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.expense
import worthy.composeapp.generated.resources.income
import worthy.composeapp.generated.resources.trend_analysis_title

@Composable
fun TrendAnalysisCard(
    transactions: List<Transaction>,
    selectedPeriod: TimePeriod
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = UiText.StringResourceId(Res.string.trend_analysis_title).asString(),
                style = AppTypography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CombinedTrendChart(
                incomeData = generateTrendData(
                    transactions,
                    TransactionType.INCOME,
                    selectedPeriod
                ),
                expenseData = generateTrendData(
                    transactions,
                    TransactionType.EXPENSE,
                    selectedPeriod
                ),
                period = selectedPeriod
            )
        }
    }
}

@Composable
fun CombinedTrendChart(
    incomeData: List<Float>,
    expenseData: List<Float>,
    period: TimePeriod
) {
    var animationProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(incomeData, expenseData, period) {
        animate(0f, 1f, animationSpec = tween(1200)) { value, _ ->
            animationProgress = value
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(240.dp, 120.dp)) {
            if (incomeData.size > 1 && expenseData.size > 1) {
                val maxValue = maxOf(incomeData.maxOrNull() ?: 1f, expenseData.maxOrNull() ?: 1f)
                val points = incomeData.size

                fun drawTrendLine(data: List<Float>, color: Color) {
                    val path = Path()
                    data.forEachIndexed { index, value ->
                        val x = (index.toFloat() / (points - 1)) * size.width * animationProgress
                        val y = size.height - (value / maxValue * size.height)

                        if (index == 0) path.moveTo(x, y)
                        else path.lineTo(x, y)
                    }

                    drawPath(
                        path = path,
                        color = color,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                drawTrendLine(incomeData, Color(0xFF4CAF50)) // Gelir
                drawTrendLine(expenseData, Color(0xFFF44336)) // Gider
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LegendItem(UiText.StringResourceId(Res.string.income).asString(), Color(0xFF4CAF50))
            LegendItem(UiText.StringResourceId(Res.string.expense).asString(), Color(0xFFF44336))
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = AppTypography.labelSmall)
    }
}

fun generateTrendData(
    transactions: List<Transaction>,
    type: TransactionType,
    period: TimePeriod
): List<Float> {
    val now = getCurrentEpochMillis()
    val oneDay = 24 * 60 * 60 * 1000L

    return (0 until period.days).map { day ->
        val dayStart = now - (day * oneDay)
        val dayEnd = dayStart + oneDay
        transactions.filter {
            it.transactionType == type &&
                    it.transactionDate in dayStart..dayEnd
        }.sumOf { it.amount.amount }.toFloat()
    }.reversed()
}
