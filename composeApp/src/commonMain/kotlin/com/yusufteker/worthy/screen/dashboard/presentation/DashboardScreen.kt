package com.yusufteker.worthy.screen.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.CategoryIcon
import com.yusufteker.worthy.core.presentation.components.IncomeAllocationCard
import com.yusufteker.worthy.core.presentation.components.PrimaryButton
import com.yusufteker.worthy.core.presentation.components.PurchaseEvaluationInfoBox
import com.yusufteker.worthy.core.presentation.theme.AppBrushes.screenBackground
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing16
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing8
import com.yusufteker.worthy.core.presentation.theme.Constants.currencySymbols
import com.yusufteker.worthy.screen.dashboard.domain.EvaluationResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.bottom_sheet_button_calculate
import worthy.composeapp.generated.resources.bottom_sheet_label_amount
import worthy.composeapp.generated.resources.bottom_sheet_label_category
import worthy.composeapp.generated.resources.bottom_sheet_select_category
import worthy.composeapp.generated.resources.close
import worthy.composeapp.generated.resources.dashboard_evaluate_purchase
import worthy.composeapp.generated.resources.dashboard_overview


@Composable
fun DashboardScreenRoot(
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateToEvaluation: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    DashboardScreen(
        state = state,
        contentPadding = contentPadding,
        onAction = { action ->
            viewModel.onAction(action)
            if (action == DashboardAction.EvaluateButtonClicked) onNavigateToEvaluation()
        },
        onWishlistClick = onNavigateToWishlist
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardState,
    contentPadding: PaddingValues = PaddingValues(),
    onAction: (DashboardAction) -> Unit,
    onWishlistClick: () -> Unit
) {


        val sheetState = rememberModalBottomSheetState()

        Column(Modifier.fillMaxSize()
            .background(
                //color = AppColors.background
                brush = screenBackground
            )
            .padding(contentPadding)
        ) {

            AppTopBar(
                title = UiText.StringResourceId(Res.string.dashboard_overview).asString(),
                onNavIconClick = null,
                isAlignCenter = true,
                isBack = false,
                modifier = Modifier.background(AppColors.transparent)
            ) {  }

            if (state.isLoading){
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }else{
                Spacer(Modifier.height(Spacing8))

                /*
                           // 1 – Progress barlar
                           LabelledProgressBar(
                               label = UiText.StringResourceId(Res.string.dashboard_savings_goal).asString(),
                               progress = state.savingProgress,
                               trailingText = "600"
                           )
                           Spacer(Modifier.height(Spacing24))
                           LabelledProgressBar(
                               label =UiText.StringResourceId(Res.string.dashboard_desires_budget).asString(),
                               progress = state.desiresSpentFraction,
                               trailingText = "750"
                           )
                           Spacer(Modifier.height(Spacing32))

                           // 2 – Monthly Income başlık
                           Text(
                               text = UiText.StringResourceId(Res.string.dashboard_monthly_income).asString(),
                               style = AppTypography.titleLarge,
                               color = AppColors.onBackground
                           )
                           Spacer(Modifier.height(Spacing16))
                           */
                // 3 – Kart
                IncomeAllocationCard(
                    modifier = Modifier.fillMaxWidth(),
                    amountText = "₺${state.monthlyIncome.toInt()}",
                    monthDeltaText = "+10%",
                    bars = listOf(
                        state.fixedExpenseFraction,
                        state.desiresSpentFraction,
                        state.expensesFraction,
                        1f - state.fixedExpenseFraction - state.desiresSpentFraction
                    ),
                    selectedChartIndex = state.selectedChartIndex,
                    onChartSelected = {
                        onAction(DashboardAction.ChartSelected(it))
                    },
                    last6MonthAmounts = state.last6MonthAmounts

                )

                Spacer(Modifier.weight(1f))

                // 4 – Evaluate Purchase
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    PrimaryButton(
                        text = UiText.StringResourceId(Res.string.dashboard_evaluate_purchase).asString(),
                        onClick = { onAction(DashboardAction.EvaluateButtonClicked) },
                        modifier = Modifier
                    )
                }
                Spacer(Modifier.height(Spacing16))

            }

        }


    if (state.isBottomSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { onAction(DashboardAction.CloseBottomSheetClicked) },
            sheetState = sheetState,
            containerColor = AppColors.surface,
            tonalElevation = 6.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            BottomSheetContent(
                sheetState = sheetState,
                onClose = {
                    onAction(DashboardAction.CloseBottomSheetClicked)
                },
                onCalculate = { amount ->
                    onAction(
                        DashboardAction.CalculateButtonClicked(amount = amount)
                    )
                },
                evaluationResult = state.evaluationResult,
                currencySymbol = currencySymbols.getValue(state.selectedCurrency),
                categories = state.categories
            )
        }
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    sheetState: SheetState = rememberModalBottomSheetState(),
    onClose: () -> Unit,
    onCalculate: (Float?) -> Unit,
    evaluationResult: EvaluationResult? = null,
    currencySymbol: String = "₺",
    categories: List<Category> = emptyList()
) {
    var amount by remember { mutableStateOf("") }
    var includeTax by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(evaluationResult != null){
        sheetState.expand()
    }



    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Harcama Hesaplayıcı", style = AppTypography.titleMedium)
        Spacer(Modifier.height(16.dp))

        evaluationResult?.let {
            PurchaseEvaluationInfoBox(
                incomePercent = it.incomePercent,
                desireBudgetPercent = it.desirePercent,
                workHoursRequired = it.workHours,
                remainingDesireBudget = it.remainingDesire,
                currencySymbol = it.currencySymbol,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Spacer(Modifier.height(16.dp))

        }

        // Fiyat girişi
        OutlinedTextField(
            value = amount,
            onValueChange = { newValue ->
                // Sadece sayı ve nokta girişine izin ver
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    amount = newValue
                }
            },
            label = { Text(UiText.StringResourceId(Res.string.bottom_sheet_label_amount).asString()) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            trailingIcon = {
                Text(
                    text = currencySymbol,
                    style = AppTypography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = AppColors.primary
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        )

        Spacer(Modifier.height(16.dp))

        // Kategori seçimi
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedCategory?.name ?: UiText.StringResourceId(Res.string.bottom_sheet_select_category).asString(),
                onValueChange = { },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(PrimaryNotEditable),
                label = { Text(UiText.StringResourceId(Res.string.bottom_sheet_label_category).asString()) },
                leadingIcon = {
                    selectedCategory?.icon?.let { icon ->
                        CategoryIcon(iconName = icon,)
                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (isDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            )

            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                               CategoryIcon(iconName = category.icon)
                                Spacer(Modifier.width(12.dp))
                                Text(category.name)
                            }
                        },
                        onClick = {
                            selectedCategory = category
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Vergi checkbox
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = includeTax, onCheckedChange = { includeTax = it })
            Text("Vergi dahil")
        }

        Spacer(Modifier.height(16.dp))

        // Hesapla butonu
        PrimaryButton(
            text = UiText.StringResourceId(Res.string.bottom_sheet_button_calculate).asString(),
            onClick = {
                onCalculate(amount.toFloatOrNull())
                /*
                val input = amount.toFloatOrNull()
                result = if (input != null && selectedCategory != null) {
                    val calculated = if (includeTax) input * 1.2f else input
                    UiText.StringResourceId(
                        id = Res.string.bottom_sheet_result,
                        args = arrayOf(calculated, selectedCategory!!.name)
                    )
                } else if (input == null) {
                    UiText.StringResourceId(Res.string.bottom_sheet_invalid_input)
                } else {
                    UiText.StringResourceId(Res.string.bottom_sheet_select_category)
                }*/

            },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.height(24.dp))
        TextButton(onClick = onClose, modifier = Modifier.align(Alignment.End)) {
            Text(UiText.StringResourceId(Res.string.close).asString())
        }
    }
}
