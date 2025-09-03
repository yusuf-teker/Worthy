package com.yusufteker.worthy.screen.analytics.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.format
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.presentation.theme.AppBrushes
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.formatMoneyText
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun TransactionListItem(
    transaction: Transaction,
    onItemClicked: (Transaction) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val amountColor = when (transaction.transactionType) {
        TransactionType.INCOME -> AppColors.transactionIncomeColor
        TransactionType.EXPENSE -> AppColors.transactionExpenseColor
        TransactionType.REFUND -> AppColors.transactionRefundColor
    }

    val dateString = transaction.transactionDate.toAppDate().format()
    Card(
        modifier = modifier.fillMaxWidth().clickable { onItemClicked(transaction) },
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(
                AppBrushes.cardItemBrushWithBorder
            ).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.name, style = AppTypography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateString,
                    style = AppTypography.bodySmall,
                    color = AppColors.onSurfaceVariant
                )
                transaction.note?.let {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = it,
                        style = AppTypography.bodySmall,
                        color = AppColors.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${if (transaction.transactionType == TransactionType.EXPENSE) "-" else "+"} ${transaction.amount.amount.formatMoneyText()} ${transaction.amount.currency.symbol}",
                color = amountColor,
                fontSize = 16.sp,
                style = AppTypography.titleMedium
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun TransactionListPreview() {
    val dummyTransactions = listOf(
        Transaction(
            id = 1,
            name = "Maaş",
            amount = Money(5000.0, Currency.TRY),
            transactionType = TransactionType.INCOME,
            categoryId = 1,
            cardId = 1,
            transactionDate = kotlin.time.Clock.System.now().toEpochMilliseconds(),
            note = "Aylık maaş"
        ), Transaction(
            id = 2,
            name = "Market Alışverişi",
            amount = Money(250.0, Currency.TRY),
            transactionType = TransactionType.EXPENSE,
            categoryId = 2,
            cardId = 2,
            transactionDate = kotlin.time.Clock.System.now().toEpochMilliseconds()
                .minus(2_592_000_000), // 30 gün önce
            note = "Gıda ve temizlik"
        ), Transaction(
            id = 3,
            name = "İade",
            amount = Money(100.0, Currency.TRY),
            transactionType = TransactionType.REFUND,
            categoryId = 3,
            cardId = 1,
            transactionDate = kotlin.time.Clock.System.now().toEpochMilliseconds().minus(86400000),
            note = "Ürün iadesi"
        )
    )

    Column {
        LazyColumn {
            items(dummyTransactions) { transaction ->
                TransactionListItem(transaction = transaction)
            }
        }
    }
}