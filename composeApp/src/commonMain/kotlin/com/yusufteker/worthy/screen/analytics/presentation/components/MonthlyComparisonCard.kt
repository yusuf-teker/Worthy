package com.yusufteker.worthy.screen.analytics.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.getMonthShortNameByLocale
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.monthly_comparison_title
import kotlin.math.max
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun MonthlyComparisonCard(transactions: List<Transaction>) {

    val currency = transactions.firstOrNull()?.amount?.currency ?: Currency.TRY
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = UiText.StringResourceId(Res.string.monthly_comparison_title).asString(),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val monthlyData = getLast6MonthsComparison(transactions).reversed()
            val maxTotal = monthlyData.maxOfOrNull { max(it.second, it.third) } ?: 0.0


            monthlyData.forEach { (month, income, expense) ->
                MonthlyComparisonItem(
                    month = month,
                    income = income,
                    expense = expense,
                    currency = currency,
                    maxTotal = maxTotal
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MonthlyComparisonItem(
    month: String,
    income: Double,
    expense: Double,
    currency: Currency,
    maxTotal: Double? = null
) {
    val balance = income - expense
    val totalAmount = maxTotal ?: (income + expense)//income + expense

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = month,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(40.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            // Income bar
            LinearProgressIndicator(
                progress = { if (totalAmount > 0) (income / totalAmount).toFloat() else 0f },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF4CAF50),
                trackColor = Color.Transparent,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
            Spacer(modifier = Modifier.height(2.dp))
            // Expense bar
            LinearProgressIndicator(
                progress = { if (totalAmount > 0) (expense / totalAmount).toFloat() else 0f },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF44336),
                trackColor = Color.Transparent,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "${if (balance >= 0) "+" else ""}${balance.toInt()} ${currency.symbol} ",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (balance >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
            ),
            modifier = Modifier.width(60.dp)
        )
    }
}

@OptIn(ExperimentalTime::class)
fun getLast6MonthsComparison(transactions: List<Transaction>): List<Triple<String, Double, Double>> {
    val timeZone = TimeZone.currentSystemDefault()
    val now = Clock.System.now().toLocalDateTime(timeZone).date
    val result = mutableListOf<Triple<String, Double, Double>>()

    for (i in 0 until 6) {
        val targetMonth = now.minus(i, DateTimeUnit.MONTH)
        val startOfMonth = LocalDate(targetMonth.year, targetMonth.month, 1)
            .atTime(0, 0)
            .toInstant(timeZone)
            .toEpochMilliseconds()

        // Ayın son gününü bul: bir sonraki ayın ilk gününden bir gün öncesi
        val nextMonth = targetMonth.plus(1, DateTimeUnit.MONTH)
        val firstDayOfNextMonth = LocalDate(nextMonth.year, nextMonth.month, 1)
        val lastDayOfTargetMonth = firstDayOfNextMonth.minus(1, DateTimeUnit.DAY)

        val endOfMonth = lastDayOfTargetMonth
            .atTime(23, 59, 59, 999_000_000)
            .toInstant(timeZone)
            .toEpochMilliseconds()

        val monthTransactions = transactions.filter { transaction ->
            transaction.transactionDate >= startOfMonth && transaction.transactionDate <= endOfMonth
        }

        val income = monthTransactions
            .filter { it.transactionType == TransactionType.INCOME }
            .sumOf { it.amount.amount }

        val expense = monthTransactions
            .filter { it.transactionType == TransactionType.EXPENSE }
            .sumOf { it.amount.amount }

        val locale = Locale.current
        val monthName =
            getMonthShortNameByLocale(targetMonth.month.number, locale.language.lowercase() == "tr")
        result.add(0, Triple(monthName, income, expense))
    }

    return result
}

