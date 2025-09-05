package com.yusufteker.worthy.screen.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import org.jetbrains.compose.resources.painterResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.ic_minus
import worthy.composeapp.generated.resources.label_budget

@Composable
fun BudgetStepper(
    modifier: Modifier = Modifier,
    color: Color = AppColors.primary,
    budgetAmount: Money,
    totalIncome: Double,
    totalFixedExpenses: Double,
    selectedCurrency: String,
    onBudgetChange: (Double) -> Unit,
) {
    val maxValue = (totalIncome - totalFixedExpenses).coerceAtLeast(0.0)

    // Step mantığı
    val step = when {
        budgetAmount.amount < 1_000 -> 10.0
        budgetAmount.amount < 100_000 -> 100.0
        else -> 1_000.0
    }

    var textValue by remember { mutableStateOf(budgetAmount.amount.toInt().toString()) }

    LaunchedEffect(budgetAmount.amount) {
        textValue = budgetAmount.amount.toInt().toString()
    }

    Column(modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(), text = UiText.StringResourceId(
                Res.string.label_budget
            ).asString(), style = AppTypography.bodyMedium, textAlign = TextAlign.Center
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            // - Button
            IconButton(
                onClick = { onBudgetChange((budgetAmount.amount - step).coerceAtLeast(0.0)) },
                enabled = budgetAmount.amount > 0
            ) {
                Icon(painterResource(Res.drawable.ic_minus), contentDescription = "Decrease")
            }

            // Input + currency yan yana
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                BasicTextField(
                    value = textValue,
                    onValueChange = { newText ->
                        val newValue =
                            (newText.filter { it.isDigit() }.toIntOrNull() ?: 0).coerceIn(
                                0, maxValue.toInt()
                            )
                        onBudgetChange(newValue.toDouble())
                        textValue = newValue.toString()

                    },
                    textStyle = AppTypography.titleMedium.copy(
                        textAlign = TextAlign.End, color = color
                    ),
                    singleLine = true,
                    modifier = Modifier.widthIn(min = 60.dp, max = 120.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = selectedCurrency,
                    style = AppTypography.titleMedium,
                    color = color
                )
            }

            // + Button
            IconButton(
                onClick = { onBudgetChange((budgetAmount.amount + step).coerceAtMost(maxValue)) },
                enabled = budgetAmount.amount < maxValue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}

