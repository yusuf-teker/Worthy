package com.yusufteker.worthy.screen.dashboard.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.getCurrentLocalDateTime
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.domain.toEpochMillis
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.AppButton
import com.yusufteker.worthy.core.presentation.components.CategoryIcon
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.theme.AppColors.secondaryButtonColors
import com.yusufteker.worthy.core.presentation.util.hideKeyboard
import io.github.aakira.napier.Napier
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.bottom_sheet_button_calculate
import worthy.composeapp.generated.resources.bottom_sheet_button_purchase
import worthy.composeapp.generated.resources.bottom_sheet_error_category_required
import worthy.composeapp.generated.resources.bottom_sheet_invalid_input
import worthy.composeapp.generated.resources.bottom_sheet_label_category
import worthy.composeapp.generated.resources.bottom_sheet_label_name
import worthy.composeapp.generated.resources.bottom_sheet_select_category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    sheetState: SheetState = rememberModalBottomSheetState(),
    onClose: () -> Unit,
    onCalculate: (Money) -> Unit,
    onPurchase: (Transaction) -> Unit,
    bottomSheetUiState: BottomSheetUiState = BottomSheetUiState(),
) {
    var amount by remember { mutableStateOf<Money?>(emptyMoney()) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var name by remember { mutableStateOf<String?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf<UiText?>(null) }
    var categoryError by remember { mutableStateOf<UiText?>(null) }
    var nameError by remember { mutableStateOf<UiText?>(null) }


    LaunchedEffect(
        bottomSheetUiState.evaluationResult != null,
        bottomSheetUiState.selectedCategory
    ) {
        Napier.d("bottom sheet is open ${bottomSheetUiState.evaluationResult})")

        sheetState.expand()
    }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    Column(
        Modifier
            .fillMaxWidth().clickable(
                indication = null, // Ripple olmasın
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
                hideKeyboard()
            }
            .padding(16.dp).verticalScroll(scrollState)
    ) {

        bottomSheetUiState.evaluationResult?.let {
            PurchaseEvaluationInfoBox(
                incomePercent = it.incomePercent,
                desireBudgetPercent = it.desirePercent,
                workHoursRequired = it.workHours,
                remainingDesireBudget = it.remainingDesire,
                incomeMinusExpensePercent = it.incomeMinusExpensePercent,
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
            isError = amountError != null,
            errorMessage = amountError
        )

        Spacer(Modifier.height(16.dp))


        bottomSheetUiState.evaluationResult?.let {
            OutlinedTextField(
                value = name ?: "",
                onValueChange = {
                    name = it
                },
                isError = nameError != null,
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        UiText.StringResourceId(Res.string.bottom_sheet_label_name).asString()
                    )
                },
                supportingText = {
                    nameError?.let {
                        Text(
                            text = it.asString(),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            // Kategori seçimi
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = {
                    isDropdownExpanded = !isDropdownExpanded
                }
            )
            {
                OutlinedTextField(
                    value = selectedCategory?.name
                        ?: UiText.StringResourceId(Res.string.bottom_sheet_select_category)
                            .asString(),
                    onValueChange = { },
                    readOnly = true,
                    isError = categoryError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(PrimaryNotEditable),
                    label = {
                        Text(
                            UiText.StringResourceId(Res.string.bottom_sheet_label_category)
                                .asString()
                        )
                    },
                    leadingIcon = if (selectedCategory?.icon != null) {
                        {
                            selectedCategory?.let {
                                CategoryIcon(iconName = it.icon)
                            }
                        }
                    } else {
                        null
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = if (isDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    supportingText = {
                        categoryError?.let {
                            Text(
                                text = it.asString(),
                                color = androidx.compose.material3.MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    bottomSheetUiState.categories.forEach { category ->
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

        }

        Spacer(Modifier.height(16.dp))

        // Hesapla butonu
        AppButton(
            text = UiText.StringResourceId(Res.string.bottom_sheet_button_calculate).asString(),
            onClick = {
                amountError = isValidAmount(amount)
                if (amountError == null) {
                    onCalculate(
                        Money(
                            amount?.amount ?: 0.0,
                            currency = amount?.currency ?: Currency.TRY
                        )
                    )
                }
            },
            loading = bottomSheetUiState.isCalculating,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        AppButton(
            text = UiText.StringResourceId(Res.string.bottom_sheet_button_purchase).asString(),
            onClick = {

                amountError = isValidAmount(amount)
                categoryError = isCategoryValid(selectedCategory)
                nameError = isNameValid(name)

                if (amountError == null && categoryError == null && nameError == null) {
                    selectedCategory?.let { category ->
                        amount?.let { amount ->
                            onPurchase(

                                Transaction(
                                    name = name ?: "",
                                    amount = amount,
                                    categoryId = category.id,
                                    transactionType = TransactionType.EXPENSE,
                                    transactionDate = getCurrentLocalDateTime().toEpochMillis(),

                                    )

                            )
                        }
                    }

                }
            },
            loading = bottomSheetUiState.isPurchasing,
            modifier = Modifier.fillMaxWidth(),
            colors = secondaryButtonColors
        )

    }
}

fun isValidAmount(amount: Money?): UiText? {
    if ((amount?.amount ?: 0.0) > 0) {
        return null
    } else {
        return UiText.StringResourceId(Res.string.bottom_sheet_invalid_input)
    }
}

fun isCategoryValid(selectedCategory: Category?): UiText? {
    return if (selectedCategory == null) {
        UiText.StringResourceId(Res.string.bottom_sheet_error_category_required)
    } else {
        null
    }
}

fun isNameValid(name: String?): UiText? {
    return if (name.isNullOrEmpty()) {
        UiText.StringResourceId(Res.string.bottom_sheet_error_category_required)
    } else {
        null
    }
}

