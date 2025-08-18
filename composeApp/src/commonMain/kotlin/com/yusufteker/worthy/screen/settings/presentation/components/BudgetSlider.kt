package com.yusufteker.worthy.screen.settings.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.toFormattedWithThousandsSeparator
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.label_budget_value

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetSlider(
    modifier: Modifier = Modifier,
    budgetAmount: Money,
    totalIncome: Double,
    totalFixedExpenses: Double,
    selectedCurrency: String,
    onBudgetChange: (Double) -> Unit,
) {
    val currency = selectedCurrency
    val maxValue = (totalIncome - totalFixedExpenses).coerceAtLeast(0.0)

    Column(modifier) {
        // ─── Metin
        Text(
            text = UiText.StringResourceId(
                id = Res.string.label_budget_value,
                args = arrayOf(budgetAmount.amount.toFormattedWithThousandsSeparator(), currency)
            ).asString(),
            style = AppTypography.bodyMedium
        )

        // ─── Kaydırma çubuğu
        Slider(
            value = budgetAmount.amount.toFloat(),
            onValueChange = { v ->
                onBudgetChange(v.toDouble().coerceIn(0.0, maxValue))
            },
            valueRange = 0f..maxValue.toFloat(),
            enabled = totalIncome > totalFixedExpenses,
            colors = SliderDefaults.colors(
                thumbColor = AppColors.primary,
                activeTrackColor = AppColors.secondary,
            )
        )
    }
}
