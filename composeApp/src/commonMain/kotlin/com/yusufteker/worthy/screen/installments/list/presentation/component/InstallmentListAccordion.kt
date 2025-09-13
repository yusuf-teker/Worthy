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
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.presentation.getMonthShortName
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.formatMoneyText
import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentCardUIModel

@Composable
fun InstallmentListAccordion(
    installments: List<InstallmentCardUIModel>, onItemClicked: (InstallmentCardUIModel) -> Unit
) {
    // groupBy ile key üret
    val groupedInstallments: Map<AppDate, List<InstallmentCardUIModel>> =
        installments.groupBy { installment ->
            val date = installment.transaction.transactionDate.toAppDate()
            AppDate(year = date.year, month = date.month) // Key
        }
    //  Ay-yıl bazlı key
    val sortedKeys = remember(groupedInstallments) {
        groupedInstallments.keys.sortedWith(compareByDescending<AppDate> { it.year }.thenByDescending { it.month })
    }

    val listState = rememberLazyListState()

    val currentMonthKey = AppDate(year = getCurrentYear(), month = getCurrentMonth())


    var expandedMonths by remember { mutableStateOf<Map<AppDate, Boolean>>(emptyMap()) }


    LaunchedEffect(sortedKeys) {
        if (sortedKeys.isNotEmpty()) {

            // Önce tüm ayları kapalı olarak ayarla
            expandedMonths = sortedKeys.associateWith { false }

            // Current month'u bul ve expand et
            val targetKey = if (sortedKeys.contains(currentMonthKey)) {
                currentMonthKey
            } else {
                sortedKeys.first() // Eğer current month yoksa ilk ay
            }

            expandedMonths = expandedMonths.toMutableMap().apply {
                this[targetKey] = true
            }


            val targetIndex = sortedKeys.indexOf(targetKey)
            if (targetIndex != -1) {
                // Her month için 2 item var (header + content), bu yüzden index * 2
                val scrollIndex = targetIndex * 2
                listState.animateScrollToItem(scrollIndex)
            }
        }
    }

    // 6) Listeyi oluştur
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        sortedKeys.forEach { monthKey ->
            val monthInstallments = groupedInstallments[monthKey].orEmpty()
            val isExpanded = expandedMonths[monthKey] == true
            val totalAmount = monthInstallments.sumOf { it.transaction.amount.amount }
            val currency =
                monthInstallments.firstOrNull()?.transaction?.amount?.currency ?: Currency.TRY

            // Header
            item(key = "header_$monthKey") {
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                        .background(AppColors.primaryContainer).clickable {
                            expandedMonths = expandedMonths.toMutableMap().apply {
                                this[monthKey] = !(this[monthKey] ?: false)
                            }
                        }.padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${getMonthShortName(monthKey.month)} ${monthKey.year}",
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
            item(key = "content_$monthKey") {
                AnimatedVisibility(visible = isExpanded) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        monthInstallments.forEach { transaction ->
                            InstallmentCard(
                                installment = transaction, onClick = { onItemClicked(transaction) })
                        }
                    }
                }
            }
        }
    }
}