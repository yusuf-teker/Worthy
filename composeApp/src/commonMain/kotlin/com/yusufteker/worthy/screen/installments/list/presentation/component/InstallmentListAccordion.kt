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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.presentation.getMonthShortName
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.formatMoneyText
import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentCardUIModel
import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentMonthUIModel

@Composable
fun InstallmentListAccordion(
    monthGroups: List<InstallmentMonthUIModel>,
    onMonthClicked: (AppDate) -> Unit,
    onItemClicked: (InstallmentCardUIModel) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        monthGroups.forEach { monthModel ->
            item(key = "header_${monthModel.date}") {
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                    .background(AppColors.primaryContainer)
                    .clickable { onMonthClicked(monthModel.date) }
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "${getMonthShortName(monthModel.date.month)} ${monthModel.date.year}",
                        style = AppTypography.titleLarge,
                        color = AppColors.onPrimaryContainer
                    )
                    Text(
                        text = monthModel.totalAmount.formatMoneyText(
                            monthModel.currency, showDecimals = false
                        ), style = AppTypography.titleLarge, color = AppColors.onPrimaryContainer
                    )
                }
            }

            item(key = "content_${monthModel.date}") {
                AnimatedVisibility(visible = monthModel.isExpanded) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        monthModel.installments.forEach { transaction ->
                            InstallmentCardV2(
                                installment = transaction, onClick = { onItemClicked(transaction) })
                        }
                    }
                }
            }
        }
    }
}
