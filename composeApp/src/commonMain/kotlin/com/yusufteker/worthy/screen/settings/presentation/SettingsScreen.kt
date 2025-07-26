package com.yusufteker.worthy.screen.settings.presentation

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.screen.settings.presentation.components.BudgetSlider
import com.yusufteker.worthy.core.presentation.components.CurrencyPicker
import com.yusufteker.worthy.screen.settings.presentation.components.FinancialWidget
import com.yusufteker.worthy.core.presentation.components.NumberPickerInput
import com.yusufteker.worthy.core.presentation.components.PieChart
import com.yusufteker.worthy.screen.settings.presentation.components.RecurringFinancialItemDialog
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.theme.Constants.WEEKLY_MAX_HOURS
import com.yusufteker.worthy.core.presentation.theme.Constants.currencySymbols
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.hour_singular
import worthy.composeapp.generated.resources.hours_plural
import worthy.composeapp.generated.resources.label_budget
import worthy.composeapp.generated.resources.label_fixed_expenses
import worthy.composeapp.generated.resources.label_income_sources
import worthy.composeapp.generated.resources.label_savings
import worthy.composeapp.generated.resources.label_weekly_work_hours
import worthy.composeapp.generated.resources.title_settings

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

    // Dialog state'leri
    var showIncomeDialog by remember { mutableStateOf(false) }
    var showFixedExpenseDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        AppTopBar(
            title = UiText.StringResourceId(Res.string.title_settings).asString(),
            onNavIconClick = {},
            isBack = false,
            isAlignCenter = false,
            modifier = Modifier.background(AppColors.transparent)
        ) {  }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // PIE CHART
            PieChart(
                values = listOf(state.totalFixedExpenses, state.budgetAmount, state.savingsAmount),
                colors = listOf(AppColors.fixedExpenseGray, AppColors.budgetBlue, AppColors.savingsGreen),
                modifier = Modifier
                    .size(120.dp)
            )


        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {


            // BUDGET SLIDER
            BudgetSlider(
                modifier = Modifier.fillMaxWidth(),
                budgetAmount = state.budgetAmount,
                totalIncome = state.totalFixedIncome,
                totalFixedExpenses = state.totalFixedExpenses,
                selectedCurrency = state.selectedCurrency,
                onBudgetChange = { v ->
                    onAction(SettingsAction.OnBudgetValueChange(v.coerceIn(0f, (state.totalFixedIncome - state.totalFixedExpenses).coerceAtLeast(0f))))
                },
                currencySymbols = currencySymbols
            )
            // INCOME WIDGET
            FinancialWidget(
                title = UiText.StringResourceId(Res.string.label_income_sources).asString(),
                totalAmount = state.totalFixedIncome,
                color = AppColors.primary,
                onClick = { showIncomeDialog = true },
                currencySymbol = currencySymbols.getValue(state.selectedCurrency)
            )

            // FIXED EXPENSES WIDGET
            FinancialWidget(
                title = UiText.StringResourceId(Res.string.label_fixed_expenses).asString(),
                totalAmount = state.totalFixedExpenses,
                color = AppColors.primary,
                onClick = { showFixedExpenseDialog = true },
                currencySymbol = currencySymbols.getValue(state.selectedCurrency)
            )
        }
        }



        // SUMMARY
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ReadOnlyRow( UiText.StringResourceId(Res.string.label_fixed_expenses).asString(), state.totalFixedExpenses,  AppColors.fixedExpenseGray,  currencySymbols.getValue(state.selectedCurrency))
            ReadOnlyRow( UiText.StringResourceId(Res.string.label_budget).asString(), state.budgetAmount, AppColors.budgetBlue, currencySymbols.getValue(state.selectedCurrency))
            ReadOnlyRow( UiText.StringResourceId(Res.string.label_savings).asString(), state.savingsAmount, AppColors.savingsGreen, currencySymbols.getValue(state.selectedCurrency))
        }


        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            val hourSingular = UiText.StringResourceId(Res.string.hour_singular).asString()
            val hoursPlural = UiText.StringResourceId(Res.string.hours_plural).asString()

            NumberPickerInput(
                label = UiText.StringResourceId(Res.string.label_weekly_work_hours).asString(),
                value = state.weeklyWorkHours,
                range = 0..WEEKLY_MAX_HOURS    ,
                step = 1,
                onValueChange = { onAction(SettingsAction.OnWeeklyWorkHoursChange(it)) },
                format = { hours ->
                    if (hours == 1) {
                        "$hours $hourSingular"
                    } else {
                        "$hours $hoursPlural"
                    }
                },
                modifier = Modifier.weight(2f)
            )
            CurrencyPicker(
                modifier = Modifier.weight(2f),
                selectedCurrencyCode = state.selectedCurrency,
                onCurrencySelected = { onAction(SettingsAction.OnCurrencyChange(it)) },
                currencySymbols = currencySymbols
            )
        }

    }

    // INCOME DIALOG

    if (showIncomeDialog){

        RecurringFinancialItemDialog(

            title = UiText.StringResourceId(Res.string.label_income_sources).asString(),
            items = state.incomeRecurringItems,
            isIncome = true,
            onDismiss = {
                showIncomeDialog = false
                        },
            onClose = {
                showIncomeDialog = false
            },
            onUpdateGroup = { items ->
                onAction(SettingsAction.OnUpdateRecurringItems(items))
            },
            onDeleteGroup = { groupId ->
                onAction(SettingsAction.OnDeleteGroupRecurringItem(groupId))
            },
            onDelete = {
                onAction(SettingsAction.OnDeleteRecurringItem(it))
            },
            onAddNew = {
                onAction(SettingsAction.OnAddNewRecurringItem(it))
            },
            //currencyCode = state.selectedCurrency,
            //isExpenseDialog = true
        )
    }

    // FIXED EXPENSES DIALOG

    if (showFixedExpenseDialog){
        RecurringFinancialItemDialog(

            title = UiText.StringResourceId(Res.string.label_fixed_expenses).asString(),
            items = state.expenseRecurringItems,
            isIncome = false,
            onDismiss = {  showFixedExpenseDialog = false },
            onClose = {
                showFixedExpenseDialog = false
            },
            onUpdateGroup = { items ->
                onAction(SettingsAction.OnUpdateRecurringItems(items))
            },
            onDeleteGroup = { groupId ->
                onAction(SettingsAction.OnDeleteGroupRecurringItem(groupId))
            },
            onDelete = {
                onAction(SettingsAction.OnDeleteRecurringItem(it))
            },
            onAddNew = {
                onAction(SettingsAction.OnAddNewRecurringItem(it))
            },
            //currencyCode = state.selectedCurrency,
            //isExpenseDialog = true
        )
    }
}


@Composable
private fun ReadOnlyRow(label: String, amount: Float, color: Color, currencySymbol: String = "₺") {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(16.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text("$label: ${amount.toInt()} $currencySymbol", style = AppTypography.bodyMedium)
    }
}