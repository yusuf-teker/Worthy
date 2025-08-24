package com.yusufteker.worthy.screen.analytics.presentation.components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.domain.toEpochMillis
import com.yusufteker.worthy.core.presentation.getMonthShortNameByLocale
import com.yusufteker.worthy.core.presentation.util.formatMoneyText
import com.yusufteker.worthy.screen.analytics.domain.TimePeriod
import kotlinx.datetime.*
import kotlin.time.ExperimentalTime

data class BarRect(val xStart: Float, val xEnd: Float, val yTop: Float, val yBottom: Float)
enum class TimeLap { DAY, WEEK }

@OptIn(ExperimentalTime::class)
@Composable
fun BarChart(transactions: List<Transaction>, selectedPeriod: TimePeriod) {
    var animationProgress by remember { mutableStateOf(0f) }
    val textMeasurer = rememberTextMeasurer()
    val barRects = remember { mutableStateListOf<BarRect>() }
    var selectedBarIndex by remember { mutableStateOf<Int?>(null) }
    val currency = transactions.firstOrNull()?.amount?.currency ?: Currency.TRY

    // Animasyon
    LaunchedEffect(selectedPeriod) {
        animationProgress = 0f
        animate(0f, 1f, animationSpec = tween(1000)) { value, _ ->
            animationProgress = value
        }
    }

    val timeLap = when (selectedPeriod) {
        TimePeriod.WEEK, TimePeriod.MONTH -> TimeLap.DAY
        else -> TimeLap.WEEK
    }

    // Gruplama
    val groupedData: List<Pair<Long, Pair<Double, Double>>> = when (timeLap) {
        TimeLap.DAY -> {
            val dayMillis = 24 * 60 * 60 * 1000
            transactions.groupBy { txn -> (txn.transactionDate / dayMillis) * dayMillis }
                .map { (key, txns) ->
                    val income = txns.filter { it.transactionType == TransactionType.INCOME }
                        .sumOf { it.amount.amount }
                    val expense = txns.filter { it.transactionType == TransactionType.EXPENSE }
                        .sumOf { it.amount.amount }
                    key to (income to expense)
                }.sortedBy { it.first }
        }

        TimeLap.WEEK -> {
            transactions.groupBy { txn ->
                val instant = Instant.fromEpochMilliseconds(txn.transactionDate)
                val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                val startOfWeek = localDate.minus(localDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
                startOfWeek.toEpochMillis()
            }.map { (key, txns) ->
                val income = txns.filter { it.transactionType == TransactionType.INCOME }
                    .sumOf { it.amount.amount }
                val expense = txns.filter { it.transactionType == TransactionType.EXPENSE }
                    .sumOf { it.amount.amount }
                key to (income to expense)
            }.sortedBy { it.first }
        }
    }

    // Minimum 7 slot
    val minSlots = 7
    val totalSlots = maxOf(minSlots, groupedData.size)

    val filledData = buildList {
        addAll(groupedData)
        repeat(totalSlots - groupedData.size) {
            add(Pair(0L, 0.0 to 0.0))
        }
    }

    if (filledData.isNotEmpty()) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .horizontalScroll(rememberScrollState())
        ) {

            // Bar açıklama
            Text(
                text = if (timeLap == TimeLap.DAY) "Her bar 1 günlük işlemler toplamını gösterir" else "Her bar 1 haftalık işlemler toplamını gösterir",
                modifier = Modifier.widthIn(max = 240.dp)
            )

            Canvas(
                modifier = Modifier
                    .width((totalSlots * 80).dp)
                    .height(300.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val clickedIndex =
                                barRects.indexOfFirst { offset.x in it.xStart..it.xEnd }
                            selectedBarIndex =
                                if (clickedIndex == selectedBarIndex) null else if (clickedIndex >= 0) clickedIndex else null
                        }
                    }
            ) {
                val barWidth = size.width / (totalSlots * 2.5f)
                val maxValue =
                    filledData.maxOfOrNull { maxOf(it.second.first, it.second.second) } ?: 1.0

                // Y ekseni çizgileri
                val steps = 4
                (0..steps).forEach { i ->
                    val value = maxValue / steps * i
                    val y = size.height - (value / maxValue * size.height * 0.7f).toFloat()
                    drawLine(Color.LightGray, Offset(0f, y), Offset(size.width, y), 1f)
                    val labelResult =
                        textMeasurer.measure(AnnotatedString(value.toInt().toString()))
                    drawText(labelResult, topLeft = Offset(0f, y - labelResult.size.height))
                }

                barRects.clear()

                // Barlar
                filledData.forEachIndexed { index, entry ->
                    val x = index * (size.width / totalSlots) + barWidth / 2
                    val incomeHeight =
                        (entry.second.first / maxValue * size.height * 0.7f * animationProgress).toFloat()
                    val expenseHeight =
                        (entry.second.second / maxValue * size.height * 0.7f * animationProgress).toFloat()
                    val barTop = size.height - maxOf(incomeHeight, expenseHeight)

                    barRects.add(BarRect(x, x + barWidth, barTop, size.height))

                    // Income
                    drawRect(
                        Color(0xFF4CAF50),
                        Offset(x, size.height - incomeHeight),
                        Size(barWidth * 0.4f, incomeHeight)
                    )
                    // Expense
                    drawRect(
                        Color(0xFFF44336),
                        Offset(x + barWidth * 0.5f, size.height - expenseHeight),
                        Size(barWidth * 0.4f, expenseHeight)
                    )

                    // X etiketi
                    val dayText =
                        if (entry.first == 0L) "" else entry.first.toLocalDateLabel(timeLap)
                    val dayResult = textMeasurer.measure(AnnotatedString(dayText))
                    drawText(dayResult, topLeft = Offset(x, size.height + 8f))
                }
            }

            // Tooltip
            selectedBarIndex?.let { index ->
                val entry = filledData[index]
                val rect = barRects[index]
                Column(
                    modifier = Modifier
                        .offset {
                            val x = (rect.xStart + rect.xEnd) / 2 + (rect.xEnd - rect.xStart) / 2
                            val y = (rect.yTop - 50).toInt().coerceAtLeast(0)
                            IntOffset(x.toInt(), y)
                        }
                        .background(Color.Black, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Income: ${entry.second.first.formatMoneyText()} ${currency.symbol}",
                        color = Color.White
                    )
                    Text(
                        text = "Expense: ${entry.second.second.formatMoneyText()} ${currency.symbol}",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
fun Long.toLocalDateLabel(timeLap: TimeLap): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val date = localDateTime.date
    val appDate = this.toAppDate()
    val locale = Locale.current
    return when (timeLap) {
        TimeLap.DAY -> "${appDate.day} ${
            getMonthShortNameByLocale(
                appDate.month,
                locale.language.lowercase() == "tr"
            )
        }"

        TimeLap.WEEK -> {
            val startOfWeek = date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY)
            val endOfWeek = startOfWeek.plus(6, DateTimeUnit.DAY)
            "${startOfWeek.day}–${endOfWeek.day} ${
                getMonthShortNameByLocale(
                    appDate.month,
                    locale.language.lowercase() == "tr"
                )
            }"
        }
    }
}
