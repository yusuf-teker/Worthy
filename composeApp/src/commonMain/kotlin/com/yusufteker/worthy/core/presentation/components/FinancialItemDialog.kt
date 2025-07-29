package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.data.database.entities.ExpenseNeedType
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.theme.Constants.currencySymbols
import com.yusufteker.worthy.core.presentation.toFormattedWithThousandsSeparator
import org.jetbrains.compose.ui.tooling.preview.Preview
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun FinancialItemDialog(
    title: String,
    items: List<ItemForDialog>,
    onDismiss: () -> Unit,
    onSave: (List<ItemForDialog>) -> Unit,
    currencyCode: String = currencySymbols.values.first(),
    isExpenseDialog: Boolean = false
) {
    var currentItems by remember { mutableStateOf(items) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<ItemForDialog?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(min = 300.dp)
            ) {
                items(currentItems) { item ->
                    FinancialItemRow(
                        item = item,
                        onDelete = {
                            currentItems = currentItems.filter { it.id != item.id }
                        },
                        onEdit = {
                            itemToEdit = item
                            showEditDialog = true
                        },
                        currencySymbol = currencySymbols.getValue(currencyCode),
                        isExpenseDialog = isExpenseDialog
                    )
                }
                item {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        IconButton(
                            onClick = { showAddDialog = true },
                            modifier = Modifier.background(AppColors.transparent)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(currentItems) }) {
                Text(UiText.StringResourceId(Res.string.save).asString())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(UiText.StringResourceId(Res.string.cancel).asString())
            }
        }
    )

    if (showAddDialog) {
        AddItemDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, amount, isFixed, scheduledDay, needType ->
                currentItems = currentItems + ItemForDialog(
                    id = Clock.System.now().toEpochMilliseconds().toInt(),
                    name = name,
                    amount = amount,
                    isFixed = isFixed,
                    scheduledDay = scheduledDay,
                    needType = needType
                )
                showAddDialog = false
            },
            currency = currencyCode,
            isExpenseDialog = isExpenseDialog
        )
    }

    if (showEditDialog && itemToEdit != null) {
        EditItemDialog(
            item = itemToEdit!!,
            onDismiss = {
                showEditDialog = false
                itemToEdit = null
            },
            onSave = { updatedItem ->
                currentItems = currentItems.map {
                    if (it.id == updatedItem.id) updatedItem else it
                }
                showEditDialog = false
                itemToEdit = null
            },
            currency = currencyCode,
            isExpenseDialog = isExpenseDialog
        )
    }
}

@Composable
private fun FinancialItemRow(
    item: ItemForDialog,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    currencySymbol: String = currencySymbols.values.first(),
    isExpenseDialog: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, style = AppTypography.bodyMedium)
            Text(
                text = UiText.StringResourceId(
                    id = Res.string.amount_with_currency,
                    arrayOf(item.amount.amount.toFormattedWithThousandsSeparator(), currencySymbol)
                ).asString(),
                style = AppTypography.bodySmall
            )
        }
        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Düzenle")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Sil")
            }
        }
    }
}

@Composable
private fun AddItemDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Money, isFixed: Boolean, scheduledDay: Int?, needType: ExpenseNeedType) -> Unit,
    currency: String = currencySymbols.values.first(),
    nameLabel: String = UiText.StringResourceId(Res.string.label_name).asString(),
    amountLabel: String = UiText.StringResourceId(Res.string.label_amount, arrayOf(currency)).asString(),
    isExpenseDialog: Boolean = false
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf<Money?>(null) }
    var isFixed by remember { mutableStateOf(true) }
    var scheduledDay by remember { mutableStateOf<Int?>(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(UiText.StringResourceId(Res.string.add_new).asString()) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(nameLabel) },
                    modifier = Modifier.fillMaxWidth()
                )

                MoneyInput(
                    // TODO LABEL DEGIS label = { Text(amountLabel) },
                    money = amount ?: Money(0.0, Currency.TRY),
                    onValueChange = { amount = it },
                    modifier = Modifier.fillMaxWidth(),
                )
                DayOfMonthSelector(
                    selectedDay = scheduledDay,
                    onDayChange = { scheduledDay = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountFloat = amount?.amount ?: 0.0
                    if (name.isNotBlank() && amountFloat > 0 && amount != null) {
                        onAdd(
                            name,
                            amount!!,
                            isFixed,
                            scheduledDay,
                            if (isExpenseDialog) ExpenseNeedType.NONE else ExpenseNeedType.NEED
                        )
                    }
                }
            ) {
                Text(UiText.StringResourceId(Res.string.add).asString())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(UiText.StringResourceId(Res.string.cancel).asString())
            }
        }
    )
}

@Composable
private fun EditItemDialog(
    item: ItemForDialog,
    onDismiss: () -> Unit,
    onSave: (ItemForDialog) -> Unit,
    currency: String = currencySymbols.values.first(),
    nameLabel: String = UiText.StringResourceId(Res.string.label_name).asString(),
    amountLabel: String = UiText.StringResourceId(Res.string.label_amount, arrayOf(currency)).asString(),
    isExpenseDialog: Boolean = false
) {
    var name by remember { mutableStateOf(item.name) }
    var amount by remember { mutableStateOf<Money>(item.amount) }
    var isFixed by remember { mutableStateOf(item.isFixed) }
    var scheduledDay by remember { mutableStateOf<Int?>(item.scheduledDay) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Düzenle") }, // Bu da bir string resource olabilir
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(nameLabel) },
                    modifier = Modifier.fillMaxWidth()
                )

                MoneyInput(
                    // TODO LABEL DEGIS label = { Text(amountLabel) },
                    money = amount,
                    onValueChange = { amount = it },
                    modifier = Modifier.fillMaxWidth(),
                )
                DayOfMonthSelector(
                    selectedDay = scheduledDay,
                    onDayChange = { scheduledDay = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountFloat = amount.amount
                    amount?.let {
                        if (name.isNotBlank() && amountFloat > 0 ) {
                            onSave(
                                item.copy(
                                    name = name,
                                    amount = it,
                                    isFixed = isFixed,
                                    scheduledDay = scheduledDay
                                )
                            )
                        }
                    }

                }
            ) {
                Text(UiText.StringResourceId(Res.string.save).asString())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(UiText.StringResourceId(Res.string.cancel).asString())
            }
        }
    )
}

data class ItemForDialog(
    val id: Int,
    val name: String,
    val amount: Money = Money(0.0, Currency.TRY),
    val currency: String = currencySymbols.values.first(),
    val isFixed: Boolean = false,
    val scheduledDay: Int?,
    val needType: ExpenseNeedType = ExpenseNeedType.NONE,
)

@OptIn(ExperimentalTime::class)
fun List<ItemForDialog>.toExpenses(): List<Expense> = map {
    Expense(
        id = it.id,
        name = it.name,
        amount = it.amount,
        isFixed = it.isFixed,
        categoryId = null,
        scheduledDay = it.scheduledDay,
        date = Clock.System.now().toEpochMilliseconds(),
        needType = it.needType,
    )
}

@OptIn(ExperimentalTime::class)
fun List<ItemForDialog>.toIncomes(): List<Income> = map {
    Income(
        id = it.id,
        name = it.name,
        amount = it.amount,
        isFixed = it.isFixed,
        scheduledDay = it.scheduledDay,
        categoryId = null,
        date = Clock.System.now().toEpochMilliseconds(),
        note = null,
    )
}

@Preview
@Composable
fun FinancialItemDialogPreview() {
    val items = listOf(
        ItemForDialog(1, "Gider 1", Money(100.0, Currency.TRY), isFixed = true, scheduledDay = -1, needType = ExpenseNeedType.NEED),
        ItemForDialog(2, "Gider 2", Money(200.0, Currency.TRY), isFixed = false, scheduledDay = -1, needType = ExpenseNeedType.WANT),
        ItemForDialog(3, "Gelir 1", Money(300.0, Currency.TRY), scheduledDay = -1)
    )
    FinancialItemDialog(
        title = "Finansal İşlemler",
        items = items,
        onDismiss = {},
        onSave = {}
    )
}