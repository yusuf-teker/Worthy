package com.yusufteker.worthy.screen.transaction.add.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.getCurrentEpochMillis
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.AppButton
import com.yusufteker.worthy.core.presentation.components.CategorySelector
import com.yusufteker.worthy.core.presentation.components.WheelDatePicker
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.components.NumberPickerInput

import com.yusufteker.worthy.core.presentation.theme.Constants.MAX_INSTALLMENT_COUNT
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add
import worthy.composeapp.generated.resources.installment_count_label
import worthy.composeapp.generated.resources.wishlist_label_product_name
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class AddTransactionFormState(
    val name: String = "",
    val money: Money? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val transactionDate: Long = getCurrentEpochMillis(),
    val note: String = "",
    val cardId: Int? = null,              // Seçilen kart
    val installmentCount: Int = 0,    // Expense taksit sayısı
    val installmentStartDate: Long = getCurrentEpochMillis() // Taksit başlangıç tarihi
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddTransactionForm(
    state: AddTransactionFormState,
    isExpense: Boolean = true,
    onNameChange: (String) -> Unit,
    onAmountChange: (Money) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onTransactionDateChange: (Long) -> Unit,
    onNoteChange: (String) -> Unit,
    onCardSelected: (cardId: Int) -> Unit,
    onInstallmentCountChange: (Int) -> Unit = {},
    onInstallmentStartDateChange: (Long) -> Unit= {},
    onNewCategoryCreated: (Category) -> Unit,

    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1 NAME
        OutlinedTextField(
            value = state.name,
            onValueChange = {
                onNameChange.invoke(it)
            },
            label = { Text(UiText.StringResourceId(Res.string.wishlist_label_product_name).asString()) },
            modifier = Modifier.fillMaxWidth()
        )
        // 2 AMOUNT
        MoneyInput(
            money = state.money?: emptyMoney(),
            onValueChange ={
                onAmountChange(it!!)
            },

        )
        // 3 CATEGORY
        CategorySelector(
            categories = state.categories,
            selectedCategory = state.selectedCategory,
            onCategorySelected = {
                onCategoryChange.invoke(it)
            },
            onNewCategoryCreated = {
                onNewCategoryCreated.invoke(it)
            },
            categoryType = if (isExpense) CategoryType.EXPENSE else CategoryType.INCOME,
        )


        Spacer(modifier = Modifier.height(2.dp))
        // Date selector
        val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

        WheelDatePicker(
            initialDate = currentDate,
            onDateSelected = { epochSeconds ->
                onTransactionDateChange(epochSeconds)
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            title = "Date Added"
        )
        val targetWeight by animateFloatAsState(
            targetValue = if (state.installmentCount > 0) 2f else 0f,
            label = "weightAnim"
        )

        if (isExpense){
            Row( // INSTALLMENT
                verticalAlignment = Alignment.Bottom
            )
            {
                Box(Modifier.weight(1f)) {
                    NumberPickerInput(

                        label = UiText.StringResourceId(Res.string.installment_count_label).asString(),
                        value = state.installmentCount,
                        range = 0..MAX_INSTALLMENT_COUNT,
                        step = 1,
                        onValueChange = {
                            onInstallmentCountChange(it)
                        },
                        modifier = Modifier.fillMaxWidth(1f),
                        errorMessage = null

                    )
                }

                if (targetWeight > 0f){
                    Spacer(Modifier.width(16.dp))

                    Box(Modifier.weight(targetWeight)){
                        WheelDatePicker(
                            initialDate = currentDate,
                            onDateSelected = { epochSeconds ->
                                onInstallmentStartDateChange(epochSeconds)
                            },
                            modifier = Modifier.wrapContentHeight(),
                            title = "Installment Start Date"
                        )
                    }
                }

            }


        }



        OutlinedTextField(
            value = state.note,
            onValueChange = onNoteChange,
            label = { Text("Note") },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.weight(1f))
        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = UiText.StringResourceId(Res.string.add).asString(),
            onClick = onSaveClick
        )
    }
}
