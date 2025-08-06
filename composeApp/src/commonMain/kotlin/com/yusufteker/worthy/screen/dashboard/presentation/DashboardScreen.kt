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
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.CategoryIcon
import com.yusufteker.worthy.core.presentation.components.IncomeAllocationCard
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.components.PrimaryButton
import com.yusufteker.worthy.core.presentation.components.PurchaseEvaluationInfoBox
import com.yusufteker.worthy.core.presentation.theme.AppBrushes.screenBackground
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing16
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing8
import com.yusufteker.worthy.screen.dashboard.domain.EvaluationResult
import kotlinx.coroutines.flow.collect
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
    onNavigateTo: (Routes) -> Unit,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is UiEvent.NavigateTo -> {
                    onNavigateTo (event.route)
                }

                else -> Unit
            }
        }
    }

    DashboardScreen(
        state = state,
        contentPadding = contentPadding,
        onAction = { action ->
            viewModel.onAction(action)
            if (action == DashboardAction.EvaluateButtonClicked) onNavigateToEvaluation()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardState,
    contentPadding: PaddingValues = PaddingValues(),
    onAction: (DashboardAction) -> Unit,
) {

    BaseContentWrapper(
        state = state
    ){
        Column(Modifier.fillMaxSize()
            .background(
                //color = AppColors.background
                brush = screenBackground
            )
            .padding(contentPadding)
        )
        {

            AppTopBar(
                title = UiText.StringResourceId(Res.string.dashboard_overview).asString(),
                onNavIconClick = null,
                isAlignCenter = true,
                isBack = false,
                modifier = Modifier.background(AppColors.transparent)
            ) {  }



                Spacer(Modifier.height(Spacing8))
                // 3 – Kart
                IncomeAllocationCard(
                    modifier = Modifier.fillMaxWidth(),
                    amountText = state.totalSelectedMonthIncomeRecurringMoney.formatted(),// todo + income eklenecek
                    incomeChangeRatio = state.incomeChangeRatio,
                    barsFractions = listOf(
                        state.fixedExpenseFraction,
                        state.desiresSpentFraction,
                        state.remainingFraction,
                        state.expensesFraction
                    ),
                    miniBarsFractions = state.selectedMiniBarsFraction,
                    miniBarsMonths = state.selectedMiniBarsMonths,


                    selectedChartIndex = state.selectedChartIndex,
                    onChartSelected = {
                        onAction(DashboardAction.ChartSelected(it))
                    },
                    selectableMonths = state.selectableMonths,
                    selectedMonth = state.selectedMonthYear,
                    onSelectedMonthChanged = { yearMonth ->
                        onAction(DashboardAction.OnSelectedMonthChanged(yearMonth))
                    },
                    onAddWishlistClicked = { onAction(DashboardAction.AddWishlistClicked) },
                    onAddRecurringClicked = { onAction(DashboardAction.AddRecurringClicked) },
                    onAddTransactionClicked = { onAction(DashboardAction.AddTransactionClicked) }

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



    val sheetState = rememberModalBottomSheetState()


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
                        DashboardAction.CalculateButtonClicked(money = amount)
                    )
                },
                evaluationResult = state.evaluationResult,
                currency = state.selectedCurrency,
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
    onCalculate: (Money) -> Unit,
    evaluationResult: EvaluationResult? = null,
    currency: Currency = Currency.TRY,
    categories: List<Category> = emptyList()
) {
    var amount by remember { mutableStateOf<Money?>(null) }
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



        MoneyInput(
            money = amount,
            onValueChange = { newValue ->
                amount = newValue
            },
            modifier = Modifier.fillMaxWidth(),

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
                onCalculate(Money(amount?.amount?: 0.0, currency = currency))
            },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.height(24.dp))
        TextButton(onClick = onClose, modifier = Modifier.align(Alignment.End)) {
            Text(UiText.StringResourceId(Res.string.close).asString())
        }
    }
}
