package com.yusufteker.worthy.screen.transactions.add.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.getCurrentEpochMillis
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.AppButton
import com.yusufteker.worthy.core.presentation.components.CardSelector
import com.yusufteker.worthy.core.presentation.components.CategorySelector
import com.yusufteker.worthy.core.presentation.components.MessageText
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.components.NumberPickerInput
import com.yusufteker.worthy.core.presentation.components.UiMessage
import com.yusufteker.worthy.core.presentation.components.WheelDatePicker
import com.yusufteker.worthy.core.presentation.components.WheelDatePickerV3
import com.yusufteker.worthy.core.presentation.theme.Constants.MAX_INSTALLMENT_COUNT
import com.yusufteker.worthy.core.presentation.util.emptyMoney
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add
import worthy.composeapp.generated.resources.date_added
import worthy.composeapp.generated.resources.installment_count_label
import worthy.composeapp.generated.resources.note
import worthy.composeapp.generated.resources.pay_with_card
import worthy.composeapp.generated.resources.wishlist_label_product_name
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class AddTransactionFormState(
    val isLoading: Boolean = false,
    val name: String = "",
    val money: Money? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val transactionDate: Long = getCurrentEpochMillis(),
    val note: String = "",
    val selectedCard: Card? = null,
    val isCardPayment: Boolean = false,
    val cards: List<Card>? = null,
    val installmentCount: Int = 1,
    //val installmentStartDate: Long = getCurrentEpochMillis(),

    val errorName: UiText? = null,
    val errorMoney: UiText? = null,
    val errorCategory: UiText? = null,
    val errorDate: UiText? = null,
    val errorCard: UiText? = null,

    )

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddTransactionForm(
    state: AddTransactionFormState,
    isExpense: Boolean = true,
    isCardPayment: Boolean = false,
    onNameChange: (String) -> Unit,
    onAmountChange: (Money) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onTransactionDateChange: (AppDate) -> Unit,
    onNoteChange: (String) -> Unit,
    onCardSelected: (card: Card) -> Unit = {},
    onInstallmentCountChange: (Int) -> Unit = {},
    onInstallmentStartDateChange: (AppDate) -> Unit = {},
    onNewCategoryCreated: (Category) -> Unit,
    onAddNewCardClicked: () -> Unit = {},
    onIsCardPaymentChanged: (Boolean) -> Unit = {},

    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1 NAME
        Column {
            OutlinedTextField(
                value = state.name,
                onValueChange = {
                    onNameChange.invoke(it)
                },
                label = {
                    Text(
                        UiText.StringResourceId(Res.string.wishlist_label_product_name).asString()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                isError = state.errorName != null
            )
            MessageText(state.errorName?.let {  UiMessage.Error(it.asString()) })
        }

        // 2 AMOUNT
        MoneyInput(
            money = state.money ?: emptyMoney(),
            onValueChange = {
                onAmountChange(it!!)
            },
            errorMessage = state.errorMoney,
            isError =  state.errorMoney != null
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
            errorMessage = state.errorCategory
        )


        Spacer(modifier = Modifier.height(2.dp))
        // Date selector

        WheelDatePickerV3(
            initialDate = state.transactionDate.toAppDate(),
            onDateSelected = { appDate ->
                onTransactionDateChange(appDate)
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            title = UiText.StringResourceId(Res.string.date_added).asString(),
            errorMessage = state.errorDate?.asString()
        )
        val targetWeight by animateFloatAsState(
            targetValue = if (state.installmentCount > 0) 2f else 0f,
            label = "weightAnim"
        )

        if (isExpense) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable {
                    onIsCardPaymentChanged(!isCardPayment)

                }
            ) {
                Checkbox(
                    checked = isCardPayment,
                    onCheckedChange = { checked ->
                        onIsCardPaymentChanged(checked)
                    }
                )
                Text(UiText.StringResourceId(Res.string.pay_with_card).asString())
            }

            AnimatedVisibility(visible = isCardPayment) {
                Column {
                    Row( // INSTALLMENT
                        verticalAlignment = Alignment.Bottom
                    )
                    {
                        Box(Modifier.weight(1f)) {
                            NumberPickerInput(

                                label = UiText.StringResourceId(Res.string.installment_count_label)
                                    .asString(),
                                value = state.installmentCount,
                                range = 1..MAX_INSTALLMENT_COUNT,
                                step = 1,
                                onValueChange = {
                                    onInstallmentCountChange(it)
                                },
                                modifier = Modifier.fillMaxWidth(1f),
                                errorMessage = null

                            )
                        }

                        /*if (targetWeight > 0f) {
                            Spacer(Modifier.width(16.dp))

                            Box(Modifier.weight(targetWeight)) {
                                WheelDatePickerV3(
                                    initialDate = getCurrentAppDate(),
                                    onDateSelected = { appDate ->
                                        onInstallmentStartDateChange(appDate)
                                    },
                                    modifier = Modifier.wrapContentHeight(),
                                    title = "Installment Start Date"
                                )
                            }
                        }*/

                    }

                    CardSelector(
                        cards = state.cards,
                        selectedCard = state.selectedCard,
                        onCardSelected = {
                            onCardSelected(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onAddNewCard = {
                            onAddNewCardClicked.invoke()
                        },
                        errorMessage = state.errorCard
                    )
                }

            }

        }



        OutlinedTextField(
            value = state.note,
            onValueChange = onNoteChange,
            label = { Text(UiText.StringResourceId(Res.string.note).asString()) },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.weight(1f))
        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = UiText.StringResourceId(Res.string.add).asString(),
            loading = state.isLoading,
            onClick = onSaveClick,
        )
    }
}
