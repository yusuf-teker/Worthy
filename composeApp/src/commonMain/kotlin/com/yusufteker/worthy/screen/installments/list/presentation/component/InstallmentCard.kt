package com.yusufteker.worthy.screen.installments.list.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import worthy.composeapp.generated.resources.transaction

@Composable
fun InstallmentCard(installment: InstallmentCardUIModel, onClick: () -> Unit) {
    val transaction = installment.transaction
    val isRefund = transaction.transactionType == TransactionType.REFUND

    val selectedDay = transaction.firstPaymentDate.toAppDate()
    val selectedLatPaymentDay = installment.card?.statementDay ?: 1

    val isPast = selectedDay.copy(day = selectedLatPaymentDay).isBeforeToday()
    val progress =
        ((transaction.installmentIndex ?: 0) + 1).toFloat() / (transaction.installmentCount
            ?: 1).toFloat()

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).padding(end = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isRefund) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_refund),
                        contentDescription = null,
                        tint = AppColors.error
                    )
                } else {
                    Text("📦") // Refund için farklı ikon

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
                                Res.string.installment_label, arrayOf(
                                    (transaction.installmentIndex ?: 0) + 1,
                                    transaction.installmentCount ?: 1
                                )
                            ).asString(),
                            style = AppTypography.bodySmall,
                            color = if (isRefund) AppColors.error.copy(alpha = 0.7f)
                            else AppColors.onSurface.copy(alpha = 0.6f)
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
                            text = if (isRefund) UiText.StringResourceId(Res.string.refund)
                                .asString() else if (isPast) UiText.StringResourceId(Res.string.installment_paid)
                                .asString() else UiText.StringResourceId(Res.string.installment_pending)
                                .asString(), style = AppTypography.bodySmall, color = when {
                                isRefund -> AppColors.error
                                isPast -> AppColors.primary
                                else -> AppColors.tertiary
                            }
                        )
                        Spacer(Modifier.height(4.dp))
                        if (!isPast) {
                            Text(
                                text = " ${installment.card?.statementDay} ${
                                    getMonthShortName(
                                        (transaction.firstPaymentDate?.toAppDate() ?: transaction.transactionDate.toAppDate()).month
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
                    modifier = Modifier.fillMaxWidth().height(4.dp),
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

@Composable
fun InstallmentCardV2(installment: InstallmentCardUIModel, onClick: () -> Unit) {
    if (installment.transaction.transactionType == TransactionType.REFUND) {
        RefundCard(installment, onClick)
    } else {
        ExpenseCard(installment, onClick)
    }
}

@Composable
fun ExpenseCard(installment: InstallmentCardUIModel, onClick: () -> Unit) {
    val transaction = installment.transaction

    val selectedDay = transaction.firstPaymentDate.toAppDate()
    val selectedLatPaymentDay = installment.card?.statementDay ?: 1

    val isPast = selectedDay.copy(day = selectedLatPaymentDay).isBeforeToday()
    val progress =
        ((transaction.installmentIndex ?: 0) + 1).toFloat() / (transaction.installmentCount
            ?: 1).toFloat()

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier.size(64.dp).background(
                    AppColors.primary.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)
                ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.transaction),
                    contentDescription = null,
                    Modifier.size(36.dp)
                )

            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(Modifier.height(64.dp).fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = transaction.name,
                        style = AppTypography.titleMedium,
                        color = AppColors.onSurface
                    )
                    Text(
                        text = transaction.amount.formatted(),
                        style = AppTypography.titleLarge,
                        color = AppColors.onSurface
                    )

                }
                Spacer(Modifier.weight(1f))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = UiText.StringResourceId(
                            Res.string.installment_label, arrayOf(
                                (transaction.installmentIndex ?: 0) + 1,
                                transaction.installmentCount ?: 1
                            )
                        ).asString(),
                        style = AppTypography.bodySmall,
                        color = AppColors.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = if (isPast) UiText.StringResourceId(Res.string.installment_paid)
                            .asString()
                            .uppercase() else UiText.StringResourceId(Res.string.installment_pending)
                            .asString().uppercase(), style = AppTypography.bodySmall,

                        fontWeight = FontWeight.Bold, letterSpacing = 1.sp,

                        color = when {
                            isPast -> AppColors.primary
                            else -> AppColors.tertiary
                        }

                    )
                }
                if (!isPast) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = " ${installment.card?.statementDay} ${
                                getMonthShortName(
                                    (transaction.firstPaymentDate?.toAppDate() ?: transaction.transactionDate.toAppDate()).month
                                )
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.onSurface
                        )
                    }
                }


                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = AppColors.primary,
                    trackColor = AppColors.surfaceVariant,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    drawStopIndicator = { },
                    gapSize = 0.dp
                )
            }
        }
    }
}

@Composable
fun RefundCard(installment: InstallmentCardUIModel, onClick: () -> Unit) {
    val transaction = installment.transaction

    val selectedDay = transaction.firstPaymentDate.toAppDate()
    val selectedLatPaymentDay = installment.card?.statementDay ?: 1
    val isPast = selectedDay.copy(day = selectedLatPaymentDay).isBeforeToday()

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Modern refund ikonu
            Box(
                modifier = Modifier.size(64.dp).background(
                    AppColors.error.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)
                ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_refund),
                    contentDescription = null,
                    tint = AppColors.error,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // İçerik
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = transaction.name,
                            style = AppTypography.titleMedium,
                            color = AppColors.onSurface
                        )
                        //if (!isPast) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${installment.card?.statementDay} ${
                                getMonthShortName(
                                    (transaction.firstPaymentDate?.toAppDate() ?: transaction.transactionDate.toAppDate()).month
                                )
                            }",
                            style = AppTypography.bodySmall,
                            color = AppColors.onSurface.copy(alpha = 0.7f)
                        )
                        //}

                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = transaction.amount.formatted(),
                            style = AppTypography.titleLarge,
                            color = AppColors.error
                        )



                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            /* Box(
                                 modifier = Modifier.background(
                                     AppColors.error, shape = RoundedCornerShape(4.dp)
                                 ).padding(horizontal = 8.dp, vertical = 2.dp)
                             ) {*/
                            Text(
                                text = UiText.StringResourceId(Res.string.refund).asString()
                                    .uppercase(),
                                style = AppTypography.bodySmall,
                                color = AppColors.error,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp

                            )
                            //}
                        }
                    }
                }

                // Çizgili stil progress bar
                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = AppColors.error.copy(alpha = 0.8f),
                    trackColor = AppColors.surfaceVariant,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    drawStopIndicator = { },
                    gapSize = 0.dp
                )

            }
        }
    }
}