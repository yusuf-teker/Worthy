package com.yusufteker.worthy.screen.installments.list.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.isBeforeToday
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.getMonthShortName
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.formatted
import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentCardUIModel
import org.jetbrains.compose.resources.painterResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.ic_refund
import worthy.composeapp.generated.resources.installment_label
import worthy.composeapp.generated.resources.installment_paid
import worthy.composeapp.generated.resources.installment_pending
import worthy.composeapp.generated.resources.refund
import worthy.composeapp.generated.resources.refund_button

@Composable
fun InstallmentCard(installment: InstallmentCardUIModel, onClick: () -> Unit) {
    val transaction = installment.transaction
    val isRefund = transaction.transactionType == TransactionType.REFUND

    val selectedDay = transaction.transactionDate.toAppDate()
    val selectedLatPaymentDay = installment.card?.statementDay ?: 1

    val isPast = selectedDay.copy(day = selectedLatPaymentDay).isBeforeToday()
    val progress =
        ((transaction.installmentIndex ?: 0) + 1).toFloat() /
                (transaction.installmentCount ?: 1).toFloat()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isRefund){
                    Icon(
                        painter = painterResource(Res.drawable.ic_refund),
                        contentDescription = null,
                        tint = AppColors.error
                    )
                }else{
                    Text( "📦") // Refund için farklı ikon

                }

            }

            Column {
                Row {
                    Column {
                        Text(
                            text = transaction.name,
                            style = AppTypography.titleMedium,
                            color = if (isRefund) AppColors.error else AppColors.onSurface
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = UiText.StringResourceId(
                                Res.string.installment_label,
                                arrayOf(
                                    (transaction.installmentIndex ?: 0) + 1,
                                    transaction.installmentCount ?: 1
                                )
                            ).asString(),
                            style = AppTypography.bodySmall,
                            color = if (isRefund)
                                AppColors.error.copy(alpha = 0.7f)
                            else
                                AppColors.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = transaction.amount.formatted(),
                            style = AppTypography.titleMedium,
                            color = if (isRefund) AppColors.error else AppColors.onSurface
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = if(isRefund) UiText.StringResourceId(Res.string.refund).asString() else if (isPast) UiText.StringResourceId(Res.string.installment_paid)
                                .asString() else UiText.StringResourceId(Res.string.installment_pending)
                                .asString(),
                            style = AppTypography.bodySmall,
                            color = when {
                                isRefund -> AppColors.error
                                isPast -> AppColors.primary
                                else -> AppColors.tertiary
                            }
                        )

                        if (!isPast) {
                            Text(
                                text = " ${installment.card?.statementDay} ${
                                    getMonthShortName(
                                        transaction.transactionDate.toAppDate().month
                                    )
                                }",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isRefund) AppColors.error else AppColors.onSurface
                            )
                        }
                    }
                }

                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = if (isRefund) AppColors.error else AppColors.primary,
                    trackColor = AppColors.surfaceVariant,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    drawStopIndicator = { },
                    gapSize = 0.dp
                )
            }
        }
    }
}
