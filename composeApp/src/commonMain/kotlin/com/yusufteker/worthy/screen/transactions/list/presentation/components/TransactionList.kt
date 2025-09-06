package com.yusufteker.worthy.screen.transactions.list.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.groupByMonth
import com.yusufteker.worthy.core.presentation.components.SwipeToDeleteWrapper
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.formatMoneyText

@Composable
fun TransactionListAccordion(
    transactions: List<Transaction>,
    convertedTransactions: List<Transaction>,
    listState: LazyListState = rememberLazyListState(),
    onDelete: (Int) -> Unit,
    onItemClicked: (transactionId: Int) -> Unit
) {
    val groupedTransactions = transactions.groupByMonth()
    val groupedConvertedTransactions = convertedTransactions.groupByMonth()
    // Hangi ayların expanded olduğunu tutalım
    var expandedMonths by remember {
        mutableStateOf(groupedTransactions.keys.mapIndexed { i, key -> key to (i == 0) }.toMap())
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groupedTransactions.forEach { (month, monthTransactions) ->
            val convertedMonthTransactions = groupedConvertedTransactions[month] ?: emptyList()
            val totalAmount = convertedMonthTransactions.sumOf { it.amount.amount }
            val currency =
                convertedMonthTransactions.firstOrNull()?.amount?.currency ?: Currency.TRY
            val isExpanded = expandedMonths[month] == true

            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                        .background(AppColors.primaryContainer).clickable {
                            expandedMonths = expandedMonths.toMutableMap().apply {
                                this[month] = !(this[month] ?: false)
                            }
                        }.padding(vertical = 12.dp)
                ) {
                    // Animated rotation for arrow icon
                    val rotationAngle by animateFloatAsState(
                        targetValue = if (isExpanded) 180f else 0f,
                        animationSpec = tween(durationMillis = 300),
                        label = "arrow_rotation"
                    )

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        modifier = Modifier.align(Alignment.CenterVertically).padding(start = 8.dp)
                            .rotate(rotationAngle),
                        tint = AppColors.onPrimaryContainer
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
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
            }

            // Animated content
            item {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { -it / 4 }) + expandVertically(
                        animationSpec = tween(300), expandFrom = Alignment.Top
                    ),
                    exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
                        animationSpec = tween(300), targetOffsetY = { -it / 4 }) + shrinkVertically(
                        animationSpec = tween(300), shrinkTowards = Alignment.Top
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        monthTransactions.forEachIndexed { index, transaction ->
                            SwipeToDeleteWrapper(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                    .animateContentSize(
                                        animationSpec = tween(300)
                                    ),
                                shape = CardDefaults.shape,
                                onDelete = { onDelete(transaction.id) }) {
                                TransactionListItem(
                                    transaction = transaction,
                                    onItemClicked = { onItemClicked(transaction.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}