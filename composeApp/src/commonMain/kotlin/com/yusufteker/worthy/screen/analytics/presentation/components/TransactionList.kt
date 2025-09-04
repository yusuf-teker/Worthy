package com.yusufteker.worthy.screen.analytics.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.groupByMonth
import com.yusufteker.worthy.core.presentation.components.SwipeToDeleteWrapper
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.formatMoney

@Composable
fun TransactionList(
    transactions: List<Transaction>,
    convertedTransactions: List<Transaction>,
    listState: LazyListState = rememberLazyListState(),
    onDelete: (Int) -> Unit
) {
    // Ay bazında grupla
    val groupedTransactions = transactions.groupByMonth()
    val groupedConvertedTransactions = convertedTransactions.groupByMonth()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        groupedTransactions.forEach { (month, monthTransactions) ->
            val convertedMonthTransactions = groupedConvertedTransactions[month] ?: emptyList()

            // Toplam tutar çevrilmiş para biriminden
            val totalAmount = convertedMonthTransactions.sumOf { it.amount.amount }
            val currency =
                convertedMonthTransactions.firstOrNull()?.amount?.currency ?: Currency.TRY

            stickyHeader {
                Row(
                    modifier = Modifier.fillMaxWidth().background(AppColors.surface).padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = month, style = AppTypography.headlineSmall)
                    Text(text = totalAmount.formatMoney(currency), style = AppTypography.bodyLarge)
                }
            }

            // Ayın transactionları
            items(monthTransactions, key = { it.id }) { transaction ->
                SwipeToDeleteWrapper(
                    modifier = Modifier.fillMaxWidth(),
                    shape = CardDefaults.shape,
                    onDelete = { onDelete(transaction.id) }) {
                    TransactionListItem(transaction = transaction)
                }
            }
        }
    }
}


data class MonthHeader(
    val month: String,
    val totalAmount: Double,
    val currency: Currency
)





@Composable
fun TransactionListFlat(
    transactions: List<Transaction>,
    convertedTransactions: List<Transaction>,
    listState: LazyListState = rememberLazyListState(),
    onDelete: (Int) -> Unit
) {
    val groupedTransactions = transactions.groupByMonth()
    val groupedConvertedTransactions = convertedTransactions.groupByMonth()

    // Flat listeye dönüştür
    val flatItems = mutableListOf<Any>()
    groupedTransactions.forEach { (month, monthTransactions) ->
        val convertedMonthTransactions = groupedConvertedTransactions[month] ?: emptyList()
        val totalAmount = convertedMonthTransactions.sumOf { it.amount.amount }
        val currency = convertedMonthTransactions.firstOrNull()?.amount?.currency ?: Currency.TRY

        flatItems.add(MonthHeader(month, totalAmount, currency))
        flatItems.addAll(monthTransactions)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(flatItems) { item ->
            when (item) {
                is MonthHeader -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(AppColors.primaryContainer)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.month,
                                style = AppTypography.titleMedium,
                                color = AppColors.onPrimaryContainer
                            )
                            Text(
                                text = item.totalAmount.formatMoney(item.currency),
                                style = AppTypography.titleMedium,
                                color = AppColors.onPrimaryContainer
                            )
                        }
                    }
                }
                is Transaction -> {
                    SwipeToDeleteWrapper(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = CardDefaults.shape,
                        onDelete = { onDelete(item.id) }
                    ) {
                        TransactionListItem(transaction = item)
                    }
                }
            }
        }
    }
}