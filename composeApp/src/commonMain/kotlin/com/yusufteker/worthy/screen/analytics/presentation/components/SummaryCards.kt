package com.yusufteker.worthy.screen.analytics.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.screen.analytics.domain.model.TimePeriod
import kotlinx.coroutines.delay
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.currency_format
import worthy.composeapp.generated.resources.summary_balance
import worthy.composeapp.generated.resources.summary_expense
import worthy.composeapp.generated.resources.summary_income
import worthy.composeapp.generated.resources.summary_refund
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun SummaryCards(transactions: List<Transaction>, selectedPeriod: TimePeriod) {
    val currentTime = Clock.System.now().toEpochMilliseconds()
    val periodStart = currentTime - (selectedPeriod.days.toDouble() * 24 * 60 * 60 * 1000)

    val currency = transactions.firstOrNull()?.amount?.currency ?: Currency.TRY
    val filteredTransactions = transactions.filter {
        if (selectedPeriod == TimePeriod.NONE) true
        else it.transactionDate >= periodStart
    }

    val totalIncome = filteredTransactions
        .filter { it.transactionType == TransactionType.INCOME }
        .sumOf { it.amount.amount }

    val totalExpense = filteredTransactions
        .filter { it.transactionType == TransactionType.EXPENSE }
        .sumOf { it.amount.amount }

    val totalRefund = filteredTransactions
        .filter { it.transactionType == TransactionType.REFUND }
        .sumOf { it.amount.amount }

    val balance = totalIncome - totalExpense + totalRefund

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            AnimatedSummaryCard(
                title = UiText.StringResourceId(Res.string.summary_income).asString(),
                value = totalIncome,
                currency = currency,
                icon = "💚",
                color = Color(0xFF4CAF50),
                delay = 0
            )
        }
        item {
            AnimatedSummaryCard(
                title = UiText.StringResourceId(Res.string.summary_expense).asString(),
                value = totalExpense,
                currency = currency,
                icon = "❤️",
                color = Color(0xFFF44336),
                delay = 150
            )
        }
        item {
            AnimatedSummaryCard(
                title = UiText.StringResourceId(Res.string.summary_refund).asString(),
                value = totalRefund,
                currency = currency,
                icon = "💙",
                color = Color(0xFF2196F3),
                delay = 300
            )
        }
        item {
            AnimatedSummaryCard(
                title = UiText.StringResourceId(Res.string.summary_balance).asString(),
                value = balance,
                currency = currency,
                icon = if (balance >= 0) "✅" else "❌",
                color = if (balance >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                delay = 450
            )
        }
    }
}

@Composable
fun AnimatedSummaryCard(
    title: String,
    value: Double,
    currency: Currency = Currency.TRY,
    icon: String,
    color: Color,
    delay: Int
) {
    var animationStarted by remember { mutableStateOf(false) }
    val animatedValue by animateIntAsState(
        targetValue = if (animationStarted) value.toInt() else 0,
        animationSpec = tween(durationMillis = 1000 + delay, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(Unit) {
        delay(delay.toLong())
        animationStarted = true
    }

    Card(
        modifier = Modifier.size(100.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.medium

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )
            Text(
                text = UiText.StringResourceId(
                    Res.string.currency_format,
                    arrayOf(animatedValue, currency.symbol),
                ).asString(),
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color,
                    fontSize = 14.sp
                )
            )
            Text(
                text = title,
                style = AppTypography.labelSmall,
                color = AppColors.onSurface.copy(alpha = 0.6f),
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

/*
@Composable
fun AnalyticsBarChart(
    transactions: List<Transaction>,
    modifier: Modifier = Modifier,
    barSpacing: Dp = 8.dp,
    maxBarHeight: Dp = 200.dp
)
{
    // 1️⃣ Transactionları aylık veya haftalık grupla
    val grouped: Map<String, List<Transaction>> = transactions.groupBy {
        val date = it.transactionDate.toAppDate()
        getMonthName(date.month).asString() + date.year
    }

    val maxAmount = grouped.values.maxOfOrNull { it.sumOf { t -> t.amount.amount } } ?: 0.0

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(barSpacing)
    ) {
        grouped.forEach { (label, txs) ->
            val income = txs.filter { it.transactionType == TransactionType.INCOME }.sumOf { it.amount.amount }
            val expense = txs.filter { it.transactionType == TransactionType.EXPENSE }.sumOf { it.amount.amount }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                // 2️⃣ Gelir barı
                Box(
                    modifier = Modifier
                        .height(maxBarHeight * if (maxAmount == 0.0) 0f else (income / maxAmount).toFloat())
                        .width(24.dp)
                        .background(Color.Green)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 3️⃣ Gider barı
                Box(
                    modifier = Modifier
                        .height(maxBarHeight * if (maxAmount == 0.0) 0f else (expense / maxAmount).toFloat())
                        .width(24.dp)
                        .background(Color.Red)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 4️⃣ X eksen etiketi
                Text(label, maxLines = 1)
            }
        }
    }
}
*/