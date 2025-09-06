package com.yusufteker.worthy.screen.transactions.list.presentation.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.formatTwoDecimals
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.top_transactions_title

@Composable
fun TopTransactionsCard(transactions: List<Transaction>) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = UiText.StringResourceId(Res.string.top_transactions_title).asString(),
                style = AppTypography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val topTransactions = transactions
                .sortedByDescending { it.amount.amount }
                .take(5)

            topTransactions.forEachIndexed { index, transaction ->
                TopTransactionItem(
                    transaction = transaction,
                    rank = index + 1,
                )
                if (index < topTransactions.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun TopTransactionItem(transaction: Transaction, rank: Int) {

    val typeColor = when (transaction.transactionType) {
        TransactionType.INCOME -> Color(0xFF4CAF50)
        TransactionType.EXPENSE -> Color(0xFFF44336)
        TransactionType.REFUND -> Color(0xFF2196F3)
    }

    val typeIcon = when (transaction.transactionType) {
        TransactionType.INCOME -> "📈"
        TransactionType.EXPENSE -> "📉"
        TransactionType.REFUND -> "🔄"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(typeColor.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                style = AppTypography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = typeColor
                )
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(text = typeIcon, fontSize = 16.sp)

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.name,
                style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            if (transaction.note != null) {
                Text(
                    text = transaction.note!!,
                    style = AppTypography.labelSmall.copy(
                        color = AppColors.onSurface.copy(alpha = 0.7f)
                    )
                )
            }
        }

        Text(
            text = "${(transaction.amount.amount).formatTwoDecimals()} ${transaction.amount.currency.symbol}",
            style = AppTypography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = typeColor
            )
        )
    }
}
