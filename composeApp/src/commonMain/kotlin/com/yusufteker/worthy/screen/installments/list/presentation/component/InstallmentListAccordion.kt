package com.yusufteker.worthy.screen.installments.list.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.groupByMonth
import com.yusufteker.worthy.core.domain.model.splitInstallments
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.presentation.getMonthShortName
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.formatMoneyText
import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentCardUIModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun InstallmentListAccordion(
    installments: List<InstallmentCardUIModel>,
    onItemClicked: (InstallmentCardUIModel) -> Unit
) {
    // Ay bazlı gruplama
    val groupedInstallments = installments.groupBy { installment ->
        val date = installment.transaction.transactionDate.toAppDate()
        "${getMonthShortName(date.month)} ${date.year}"
    }

    // Hangi ayların expanded olduğunu tut
    var expandedMonths by remember {
        mutableStateOf(groupedInstallments.keys.mapIndexed { i, key -> key to (i == 0) }.toMap())
    }
    val listState = rememberLazyListState()
    val currentMonthStr = "${getMonthShortName(getCurrentMonth())} ${getCurrentYear()}"

    LaunchedEffect(groupedInstallments) {
        val index = groupedInstallments.keys.indexOf(currentMonthStr)
        if (index >= 0) {
            expandedMonths = expandedMonths.toMutableMap().apply { this[currentMonthStr] = true }
            listState.animateScrollToItem(index)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groupedInstallments.forEach { (month, monthInstallments) ->
            val isExpanded = expandedMonths[month] == true
            val totalAmount = monthInstallments.sumOf { it.transaction.amount.amount }
            val currency = monthInstallments.firstOrNull()?.transaction?.amount?.currency ?: Currency.TRY

            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppColors.primaryContainer)
                        .clickable {
                            expandedMonths = expandedMonths.toMutableMap().apply {
                                this[month] = !(this[month] ?: false)
                            }
                        }
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = month,
                        style = AppTypography.titleLarge,
                        color = AppColors.onPrimaryContainer
                    )
                    Text(
                        text = totalAmount.formatMoneyText(currency, showDecimals = false),
                        style = AppTypography.titleLarge,
                        color = AppColors.onPrimaryContainer
                    )
                }
            }

            // Expanded content
            item {
                AnimatedVisibility(visible = isExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        monthInstallments.forEach { transaction ->
                            InstallmentCard(
                                installment = transaction,
                                onClick = {

                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
