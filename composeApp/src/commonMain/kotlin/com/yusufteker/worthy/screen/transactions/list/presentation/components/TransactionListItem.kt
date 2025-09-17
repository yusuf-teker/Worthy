package com.yusufteker.worthy.screen.transactions.list.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.format
import com.yusufteker.worthy.core.domain.model.isInstallment
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppBrushes
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.formatMoneyText
import org.jetbrains.compose.ui.tooling.preview.Preview
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.installment_label
import worthy.composeapp.generated.resources.monthly
import worthy.composeapp.generated.resources.refund
import worthy.composeapp.generated.resources.refund_button
import worthy.composeapp.generated.resources.subscription
import worthy.composeapp.generated.resources.transaction
import kotlin.math.absoluteValue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun TransactionListItem(
    transaction: Transaction,
    onItemClicked: (Transaction) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val amountColor = when {
        transaction is Transaction.SubscriptionTransaction -> AppColors.txInstallmentColor
        transaction.transactionType == TransactionType.EXPENSE && transaction.isInstallment() ->
            AppColors.txInstallmentColor
        transaction.transactionType == TransactionType.EXPENSE -> AppColors.txExpenseColor
        transaction.transactionType == TransactionType.INCOME -> AppColors.txIncomeColor
        transaction.transactionType == TransactionType.REFUND -> AppColors.txRefundColor
        else -> Color.Gray
    }

    val dateString = transaction.transactionDate.toAppDate().format()
    val firstPaymentDateString = transaction.firstPaymentDate.toAppDate().format()
    Card(
        modifier = modifier.fillMaxWidth().clickable { onItemClicked(transaction) },
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).background(
                AppBrushes.cardItemBrushWithBorder
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Text(
                    text = transaction.name, style = AppTypography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateString,
                    style = AppTypography.bodySmall,
                    color = AppColors.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = firstPaymentDateString,
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

            Column(
                modifier = Modifier.padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(// todo refundda + olmicak
                    text = "${if (transaction.transactionType == TransactionType.EXPENSE) "-" else "+"} ${
                        transaction.amount.amount.absoluteValue.formatMoneyText(
                            currency = transaction.amount.currency, showDecimals = true
                        )
                    }", color = amountColor, fontSize = 16.sp, style = AppTypography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))

                val labelText = when (transaction) {
                    is Transaction.NormalTransaction -> {

                        if (transaction.transactionType == TransactionType.REFUND){
                            UiText.StringResourceId(Res.string.refund).asString()
                        }
                        else if (transaction.isInstallment())
                            UiText.StringResourceId(Res.string.installment_label,arrayOf( (transaction.installmentIndex?:0)+1, transaction.installmentCount?:1)).asString()
                        else  UiText.StringResourceId(Res.string.transaction)
                           .asString()
                    }

                    is Transaction.SubscriptionTransaction -> UiText.StringResourceId(Res.string.subscription)
                        .asString()

                    is Transaction.RecurringTransaction -> UiText.StringResourceId(Res.string.monthly)
                        .asString()
                }

                Box(
                    modifier = Modifier.border(
                            width = 1.dp, color = amountColor, shape = MaterialTheme.shapes.small
                        ).clip(MaterialTheme.shapes.small).background(Color.Transparent)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = labelText,
                        style = AppTypography.bodySmall,
                        color = amountColor,
                    )
                }

            }

        }

    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun TransactionListPreview() {
    val dummyTransactions = listOf(
        Transaction.NormalTransaction(
            id = 1,
            name = "Maaş",
            amount = Money(5000.0, Currency.TRY),
            transactionType = TransactionType.INCOME,
            categoryId = 1,
            cardId = 1,
            transactionDate = Clock.System.now().toEpochMilliseconds(),
            installmentIndex = -1,
            note = "Aylık maaş"
        ), Transaction.NormalTransaction(
            id = 2,
            name = "Market Alışverişi",
            amount = Money(250.0, Currency.TRY),
            transactionType = TransactionType.EXPENSE,
            categoryId = 2,
            cardId = 2,
            transactionDate = Clock.System.now().toEpochMilliseconds()
                .minus(2_592_000_000), // 30 gün önce
            installmentIndex = -1,
            note = "Gıda ve temizlik"
        ), Transaction.NormalTransaction(
            id = 3,
            name = "İade",
            amount = Money(100.0, Currency.TRY),
            transactionType = TransactionType.REFUND,
            categoryId = 3,
            cardId = 1,
            transactionDate = Clock.System.now().toEpochMilliseconds().minus(86400000),
            installmentIndex = -1,
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