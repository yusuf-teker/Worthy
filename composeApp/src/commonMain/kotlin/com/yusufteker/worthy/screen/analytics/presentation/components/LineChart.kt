package com.yusufteker.worthy.screen.analytics.presentation.components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.yusufteker.worthy.core.domain.getCurrentEpochMillis
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.presentation.getMonthShortNameByLocale
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.theme.Constants.ONE_DAY_MILLIS
import com.yusufteker.worthy.core.presentation.util.formatMoneyText
import com.yusufteker.worthy.screen.analytics.domain.model.TimePeriod
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.pow

data class ChartPoint(
    val x: Float,
    val y: Float,
    val value: Double,
    val date: Long
)

@Composable
fun LineChart(
    transactions: List<Transaction>, selectedPeriod: TimePeriod
) {
    var animationProgress by remember { mutableStateOf(0f) }
    LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    LaunchedEffect(selectedPeriod) {
        animationProgress = 0f
        animate(0f, 1f, animationSpec = tween(1500)) { value, _ ->
            animationProgress = value
        }
    }

    val currentTime = getCurrentEpochMillis()
    val periodStart = currentTime - (selectedPeriod.days.toDouble() * ONE_DAY_MILLIS)
    val currency = transactions.firstOrNull()?.amount?.currency ?: Currency.TRY

    // SADECE TRANSACTİON DEĞERLERİ
    /*val dailyData = transactions
        .filter {
            Napier.d("currentTime=$currentTime")
            Napier.d("selectedPeriod.days=${selectedPeriod.days}")
            Napier.d("periodStart=$periodStart")

            it.transactionDate >= periodStart
        }
        .groupBy {
            val dayMillis = 24 * 60 * 60 * 1000
            (it.transactionDate / dayMillis) * dayMillis
        }
        .mapValues { entry ->
            val income = entry.value.filter { it.transactionType == TransactionType.INCOME }
                .sumOf { it.amount.amount }
            val expense = entry.value.filter { it.transactionType == TransactionType.EXPENSE }
                .sumOf { it.amount.amount }
            income - expense
        }
        .toList()
        .sortedBy { it.first }*/
    // KÜMÜLATİF OLARAK
    val dailyData: List<Pair<Long, Double>> = transactions.filter {
        if (selectedPeriod == TimePeriod.NONE) true
        else it.transactionDate >= periodStart
    }.groupBy {
        val dayMillis = ONE_DAY_MILLIS
        (it.transactionDate / dayMillis) * dayMillis
    }.map { entry ->
        val dailyTotal = entry.value.fold(0.0) { acc, transaction ->
            when (transaction.transactionType) {
                TransactionType.INCOME -> acc + transaction.amount.amount
                TransactionType.EXPENSE -> acc - transaction.amount.amount
                TransactionType.REFUND -> acc // ignore
            }
        }
        entry.key to dailyTotal // Pair<Long, Double>
    }.runningFold(0L to 0.0) { acc, pair ->
        val (_, cumulative) = acc
        val (day, dailyValue) = pair
        day to (cumulative + dailyValue)
    }.sortedBy { it.first }

        .drop(1) // İlk dummy değeri atıyoruz

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp).horizontalScroll(scrollState)
    ) {
        var selectedPoint by remember { mutableStateOf<ChartPoint?>(null) }
        val baseWidthPerDay = 60.dp
        val screenWidth = 600.dp
        val minDays = 10 // Minimum gösterilecek gün sayısı
        val displayDays = maxOf(dailyData.size, minDays)
        val dataWidth = displayDays * baseWidthPerDay
        val canvasWidth = maxOf(screenWidth, dataWidth)


        if (dailyData.isNotEmpty()) {
            Canvas(
                modifier = Modifier.width(canvasWidth).fillMaxHeight()
                    .pointerInput(dailyData) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val position = event.changes.first().position
                                val chartPadding = 50.dp.toPx()

                                val dataWidthPx = dataWidth.toPx()

                                val points = dailyData.mapIndexed { index, entry ->
                                    val x =
                                        chartPadding + (index.toFloat() / (displayDays - 1)) * (dataWidthPx - 2 * chartPadding)
                                    val maxValue = dailyData.maxOfOrNull { abs(it.second) } ?: 1.0
                                    val y =
                                        size.height / 2 - (entry.second.toFloat() / maxValue.toFloat()) * (size.height / 4)
                                    ChartPoint(x, y, entry.second, entry.first)
                                }

                                val nearest = points.minByOrNull { point ->
                                    (point.x - position.x).pow(2) + (point.y - position.y).pow(2)
                                }

                                selectedPoint =
                                    if (nearest != null && (nearest.x - position.x).absoluteValue < 24.dp.toPx() && (nearest.y - position.y).absoluteValue < 24.dp.toPx()) nearest else null
                            }
                        }
                    }
            ) {
                val chartPadding = 50.dp.toPx()
                val dataWidthPx = dataWidth.toPx()
                val maxValue = dailyData.maxOfOrNull { abs(it.second) } ?: 1.0
                val zeroY = size.height / 2

                // Text style for labels
                val textStyle = TextStyle(
                    color = Color.Gray, fontSize = 12.sp
                )

                // Points hesaplaması (grid için de kullanacağız)
                val points = dailyData.mapIndexed { index, entry ->
                    val x =
                        chartPadding + (index.toFloat() / (displayDays - 1)) * (dataWidthPx - 2 * chartPadding)
                    val y =
                        size.height / 2 - (entry.second.toFloat() / maxValue.toFloat()) * (size.height / 4)
                    ChartPoint(x, y, entry.second, entry.first)
                }

                // Minimum gün sayısı kadar dikey grid çizgileri
                for (i in 0 until displayDays) {
                    val x =
                        chartPadding + (i.toFloat() / (displayDays - 1)) * (dataWidthPx - 2 * chartPadding)
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.3f),
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Yatay grid çizgileri
                val yLabels = listOf(maxValue, maxValue / 2, 0.0, -maxValue / 2, -maxValue)
                yLabels.forEachIndexed { index, value ->
                    val y =
                        size.height / 4 + (index.toFloat() / (yLabels.size - 1)) * (size.height / 2)

                    // Yatay grid çizgisi
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.3f),
                        start = Offset(chartPadding, y),
                        end = Offset(dataWidthPx - chartPadding, y),
                        strokeWidth = 1.dp.toPx()
                    )

                    // Y ekseni değerleri (sol taraf)
                    val textLayoutResult = textMeasurer.measure("${value.toInt()}", textStyle)
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "${value.toInt()}",
                        style = textStyle,
                        topLeft = Offset(10f, y - textLayoutResult.size.height / 2)
                    )
                }

                // Zero line (özel vurgulu)
                drawLine(
                    color = Color.Gray,
                    start = Offset(chartPadding, zeroY),
                    end = Offset(dataWidthPx - chartPadding, zeroY),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f))
                )

                // "0" yazısını zero line'ın başına ekle
                val zeroTextLayoutResult = textMeasurer.measure("0", textStyle)
                drawText(
                    textMeasurer = textMeasurer,
                    text = "0",
                    style = textStyle,
                    topLeft = Offset(10f, zeroY - zeroTextLayoutResult.size.height / 2)
                )
                // X ekseni günleri (alt kısım) - sadece veri olan günleri göster
                points.forEachIndexed { index, point ->
                    if (index % 2 == 0 || dailyData.size <= 10) { // Her iki günde bir göster veya az veri varsa hepsini göster

                        val appDate = dailyData[index].first.toAppDate()
                        val dayText = appDate.day.toString() + getMonthShortNameByLocale(
                            appDate.month
                        )

                        val dayTextLayoutResult = textMeasurer.measure(dayText, textStyle)
                        drawText(
                            textMeasurer = textMeasurer,
                            text = dayText,
                            style = textStyle,
                            topLeft = Offset(
                                point.x - dayTextLayoutResult.size.width / 2,
                                size.height - dayTextLayoutResult.size.height - 10f
                            )
                        )
                    }
                }

                if (points.size > 1) {
                    // Üst alan (pozitif değerler) - yeşil
                    val positiveAreaPath = Path().apply {
                        moveTo(points.first().x, zeroY)
                        points.forEach { point ->
                            val animatedY = zeroY + (point.y - zeroY) * animationProgress
                            val clampedY =
                                minOf(animatedY, zeroY) // 0 çizgisinin üstünde kalması için
                            lineTo(point.x * animationProgress, clampedY)
                        }
                        lineTo(points.last().x * animationProgress, zeroY)
                        close()
                    }
                    drawPath(
                        path = positiveAreaPath, brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF4CAF50).copy(alpha = 0.3f), Color.Transparent)
                        )
                    )

                    // Alt alan (negatif değerler) - kırmızı
                    val negativeAreaPath = Path().apply {
                        moveTo(points.first().x, zeroY)
                        points.forEach { point ->
                            val animatedY = zeroY + (point.y - zeroY) * animationProgress
                            val clampedY =
                                maxOf(animatedY, zeroY) // 0 çizgisinin altında kalması için
                            lineTo(point.x * animationProgress, clampedY)
                        }
                        lineTo(points.last().x * animationProgress, zeroY)
                        close()
                    }
                    drawPath(
                        path = negativeAreaPath, brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFFE53E3E).copy(alpha = 0.3f))
                        )
                    )

                    // Line
                    val linePath = Path().apply {
                        moveTo(points.first().x, points.first().y)
                        points.drop(1).forEach { point ->
                            val animatedY = zeroY + (point.y - zeroY) * animationProgress
                            lineTo(point.x * animationProgress, animatedY)
                        }
                    }
                    drawPath(
                        path = linePath,
                        color = Color(0xFF4CAF50),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Points
                    points.forEach { point ->
                        val animatedY = zeroY + (point.y - zeroY) * animationProgress
                        drawCircle(
                            color = Color(0xFF4CAF50),
                            radius = 6.dp.toPx(),
                            center = Offset(point.x * animationProgress, animatedY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3.dp.toPx(),
                            center = Offset(point.x * animationProgress, animatedY)
                        )
                    }
                }
            }

            selectedPoint?.let { point ->
                Column(modifier = Modifier.offset {
                    IntOffset(
                        point.x.toInt(), (point.y - 40).toInt()
                    )
                }.background(Color.Black, shape = RoundedCornerShape(8.dp)).padding(4.dp)) {
                    Text(
                        text = "${point.date.toAppDate()}",
                        color = Color.White,
                        style = AppTypography.bodyMedium,

                        )
                    Text(
                        text = "${point.value.formatMoneyText()} ${currency.symbol}",
                        color = Color.White,
                        style = AppTypography.bodyMedium,
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Veri yok", style = AppTypography.bodyLarge)
            }
        }
    }
}