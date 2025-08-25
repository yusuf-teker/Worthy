package com.yusufteker.worthy.screen.analytics.presentation.components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.getColorByIndex
import com.yusufteker.worthy.core.domain.model.getNameResource
import com.yusufteker.worthy.core.presentation.UiText.StringResourceId
import com.yusufteker.worthy.core.presentation.formatTwoDecimals
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.chart_category_analysis_expense
import worthy.composeapp.generated.resources.chart_category_analysis_income
import worthy.composeapp.generated.resources.chart_category_analysis_refund

@Composable
fun CategoryAnalysisCard(
    transactions: List<Transaction>, categories: List<Category>, type: TransactionType,
) {

    val currency = transactions.firstOrNull()?.amount?.currency ?: Currency.TRY
    val totalAmount = transactions.sumOf { it.amount.amount }


    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = when (type) {
                TransactionType.INCOME -> StringResourceId(Res.string.chart_category_analysis_income).asString()
                TransactionType.EXPENSE -> StringResourceId(Res.string.chart_category_analysis_expense).asString()
                TransactionType.REFUND -> StringResourceId(Res.string.chart_category_analysis_refund).asString()
            },
            style = AppTypography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        categories.forEachIndexed { index, category ->
            val categoryTotal = transactions.filter { it.categoryId == category.id }
                .sumOf { it.amount.amount }

            if (categoryTotal > 0) {
                CategoryAnalysisItem(
                    category = category,
                    amount = categoryTotal,
                    percentage = if (totalAmount > 0) (categoryTotal / totalAmount * 100).toFloat() else 0f,
                    index = index,
                    currency = currency
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}

@Composable
fun CategoryAnalysisItem(
    category: Category,
    amount: Double,
    percentage: Float,
    index: Int,
    currency: Currency
) {
    var animationProgress by remember { mutableStateOf(0f) }
    LaunchedEffect(category.id) {
        animate(0f, 1f, animationSpec = tween(1000)) { value, _ ->
            animationProgress = value
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(getColorByIndex(index), CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.getNameResource(),
                style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )

            LinearProgressIndicator(
                progress = { (percentage / 100f) * animationProgress },
                modifier = Modifier.fillMaxWidth(),
                color = getColorByIndex(index),
                trackColor = getColorByIndex(index).copy(alpha = 0.2f),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${amount.formatTwoDecimals()} ${currency.symbol}",
                style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "${(percentage * animationProgress).formatTwoDecimals()}%",
                style = AppTypography.labelSmall.copy(
                    color = AppColors.onSurface.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
fun CategoryAnalysisPager(
    transactions: List<Transaction>, categories: List<Category>,
    backgroundColor: Color = AppColors.secondaryContainer,
) {
    val typeList: MutableList<TransactionType> = mutableListOf()
    // öncelik  expense income refund
    if (transactions.any { it.transactionType == TransactionType.EXPENSE }) {
        typeList.add(TransactionType.EXPENSE)
    }
    if (transactions.any { it.transactionType == TransactionType.INCOME }) {
        typeList.add(TransactionType.INCOME)
    }

    if (transactions.any { it.transactionType == TransactionType.REFUND }) {
        typeList.add(TransactionType.REFUND)
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { typeList.size }
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(),

            ) { page ->
            val filteredTransactions = transactions.filter { it.transactionType == typeList[page] }
            CategoryAnalysisCard(
                transactions = filteredTransactions,
                categories = categories,
                type = typeList[page]
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Pager indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            typeList.forEachIndexed { index, _ ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier.size(if (isSelected) 12.dp else 8.dp).clip(CircleShape)
                        .background(
                            if (isSelected) AppColors.primary
                            else AppColors.onSurface.copy(alpha = 0.3f)
                        )
                )
                if (index < typeList.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

    }
}