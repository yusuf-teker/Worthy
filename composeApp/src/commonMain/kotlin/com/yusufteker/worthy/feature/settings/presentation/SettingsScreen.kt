package com.yusufteker.worthy.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.FinancialItemDialog
import com.yusufteker.worthy.core.presentation.components.FinancialWidget
import com.yusufteker.worthy.core.presentation.components.ItemForDialog
import com.yusufteker.worthy.core.presentation.components.PieChart
import com.yusufteker.worthy.core.presentation.components.PrimaryButton
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.feature.settings.domain.ExpenseItem
import com.yusufteker.worthy.feature.settings.domain.IncomeItem
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.label_budget
import worthy.composeapp.generated.resources.label_budget_value
import worthy.composeapp.generated.resources.label_fixed_expenses
import worthy.composeapp.generated.resources.label_income_sources
import worthy.composeapp.generated.resources.label_savings
import worthy.composeapp.generated.resources.save

@Composable
fun SettingsScreenRoot(
    viewModel:SettingsViewModel = koinViewModel(),
    contentPadding : PaddingValues = PaddingValues(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onAction = { action ->
            when (action) {

                else -> Unit
            }
            viewModel.onAction(action)
        },
        contentPadding = contentPadding
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {

    // Hesaplanan değerler
    val totalIncome by derivedStateOf { state.incomeItems.sumOf { it.amount.toDouble() }.toFloat() }
    val totalFixedExpenses by derivedStateOf { state.fixedExpenseItems.sumOf { it.amount.toDouble() }.toFloat() }
    val saving by derivedStateOf { (totalIncome - totalFixedExpenses - state.budgetAmount).coerceAtLeast(0f) }

    // Dialog state'leri
    var showIncomeDialog by remember { mutableStateOf(false) }
    var showFixedExpenseDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // PIE CHART
        PieChart(
            values = listOf(totalFixedExpenses, state.budgetAmount, saving),
            colors = listOf(AppColors.primary, AppColors.secondary, AppColors.tertiary),
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        // BUDGET SLIDER
        Column {
            Text(
                text = UiText.StringResourceId(id = Res.string.label_budget_value, arrayOf(state.budgetAmount.toInt())).asString(),
                style = AppTypography.bodyMedium)

            Slider(
                value = state.budgetAmount,
                onValueChange = { v ->
                    onAction(SettingsAction.OnBudgetValueChange(v.coerceIn(0f, (totalIncome - totalFixedExpenses).coerceAtLeast(0f))))
                },
                valueRange = 0f..(totalIncome - totalFixedExpenses).coerceAtLeast(0f),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Transparent,
                    activeTrackColor = AppColors.secondary,
                ),
                enabled = totalIncome > totalFixedExpenses,
            )
        }

        // INCOME WIDGET
        FinancialWidget(
            title = UiText.StringResourceId(Res.string.label_income_sources).asString(),
            totalAmount = totalIncome,
            color = AppColors.primary,
            onClick = { showIncomeDialog = true }
        )

        // FIXED EXPENSES WIDGET
        FinancialWidget(
            title = UiText.StringResourceId(Res.string.label_fixed_expenses).asString(),
            totalAmount = totalFixedExpenses,
            color = AppColors.primary,
            onClick = { showFixedExpenseDialog = true }
        )

        // SUMMARY
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ReadOnlyRow( UiText.StringResourceId(Res.string.label_fixed_expenses).asString(), totalFixedExpenses, AppColors.primary)
            ReadOnlyRow( UiText.StringResourceId(Res.string.label_budget).asString(), state.budgetAmount, AppColors.secondary)
            ReadOnlyRow( UiText.StringResourceId(Res.string.label_savings).asString(), saving, AppColors.tertiary)
        }
    }

    // INCOME DIALOG
    if (showIncomeDialog) {
        FinancialItemDialog(
            title =  UiText.StringResourceId(Res.string.label_income_sources).asString(),
            items = state.incomeItems.map { ItemForDialog(it.id, it.name, it.amount) } ?: listOf(),
            onDismiss = { showIncomeDialog = false },
            onSave = { newItems ->
                onAction(SettingsAction.OnSaveIncomeItems( newItems.map { IncomeItem(it.id, it.name, it.amount) }))
                showIncomeDialog = false
            }
        )
    }

    // FIXED EXPENSES DIALOG
    if (showFixedExpenseDialog) {
        FinancialItemDialog(
            title = UiText.StringResourceId(Res.string.label_fixed_expenses).asString(),
            items = state.fixedExpenseItems.map { ItemForDialog(it.id, it.name, it.amount) } ?: listOf(),
            onDismiss = { showFixedExpenseDialog = false },
            onSave = { newItems ->
                onAction(SettingsAction.OnSaveExpenseItems( newItems.map { ExpenseItem(it.id, it.name, it.amount) }))
                showFixedExpenseDialog = false
            }
        )
    }
}

@Composable
private fun ReadOnlyRow(label: String, amount: Float, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(16.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text("$label: ${amount.toInt()}₺", style = AppTypography.bodyMedium)
    }
}