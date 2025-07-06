package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.label_budget_value

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetSlider(
    modifier: Modifier = Modifier,
    budgetAmount: Float,
    totalIncome: Float,
    totalFixedExpenses: Float,
    selectedCurrency: String,
    onBudgetChange: (Float) -> Unit,
    currencySymbols: Map<String, String> = mapOf(
        "TRY" to "₺", "USD" to "$", "EUR" to "€", "GBP" to "£", "JPY" to "¥"
    ),
) {
    val currency = currencySymbols.getValue(selectedCurrency)
    val maxValue = (totalIncome - totalFixedExpenses).coerceAtLeast(0f)

    Column(modifier) {
        // ─── Metin
        Text(
            text = UiText.StringResourceId(
                id = Res.string.label_budget_value,
                args = arrayOf(budgetAmount.toInt(), currency)
            ).asString(),
            style = AppTypography.bodyMedium
        )

        // ─── Kaydırma çubuğu
        Slider(
            value = budgetAmount,
            onValueChange = { v -> onBudgetChange(v.coerceIn(0f, maxValue)) },
            valueRange = 0f..maxValue,
            enabled = totalIncome > totalFixedExpenses,
            colors = SliderDefaults.colors(
                thumbColor = AppColors.primary,
                activeTrackColor = AppColors.secondary,
            )
        )
    }
}
