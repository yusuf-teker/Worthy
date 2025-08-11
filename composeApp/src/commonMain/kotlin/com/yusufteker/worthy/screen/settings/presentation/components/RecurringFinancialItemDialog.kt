package com.yusufteker.worthy.screen.settings.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.data.database.entities.ExpenseNeedType
import com.yusufteker.worthy.core.domain.createTimestampId
import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem
import com.yusufteker.worthy.core.domain.model.currentAppDate
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.domain.model.endDate
import com.yusufteker.worthy.core.domain.model.setMonth
import com.yusufteker.worthy.core.domain.model.startDate
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.DateSelector
import com.yusufteker.worthy.core.presentation.components.DayOfMonthSelector
import com.yusufteker.worthy.core.presentation.components.DropdownMenuSelector
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.components.SwipeToDeleteWrapper
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_expense
import worthy.composeapp.generated.resources.add_income
import worthy.composeapp.generated.resources.amount_must_be_greater_than_zero
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.close
import worthy.composeapp.generated.resources.date_ranges_conflict
import worthy.composeapp.generated.resources.expense_name
import worthy.composeapp.generated.resources.expenses
import worthy.composeapp.generated.resources.income_name
import worthy.composeapp.generated.resources.incomes
import worthy.composeapp.generated.resources.missing_month_or_year
import worthy.composeapp.generated.resources.new_amount
import worthy.composeapp.generated.resources.save
import worthy.composeapp.generated.resources.start_date_after_end_date
import worthy.composeapp.generated.resources.start_date_must_be_before
import worthy.composeapp.generated.resources.start_dates_cannot_be_same
import worthy.composeapp.generated.resources.update
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun RecurringFinancialItemDialog(
    title: String,
    items: List<RecurringFinancialItem>,
    isIncome: Boolean = false,
    onDismiss: () -> Unit,
    onClose: () -> Unit,
    onUpdateGroup: (List<RecurringFinancialItem>) -> Unit= {},
    onDeleteGroup: (String)-> Unit = {},
    onDelete: (RecurringFinancialItem) -> Unit = {},
    onAddNew: (RecurringFinancialItem) -> Unit = {},

    ) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<RecurringFinancialItem?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {

            val visibleItemIds = remember { mutableStateMapOf<String, Boolean>() }

            LazyColumn(modifier = Modifier.heightIn(min = 300.dp)) {


                items( // TODO SIRALAMA SONRA DUZELECEK
                    items
                        .groupBy { it.name } // Önce isme göre grupla (örneğin "Maaş")
                        .entries
                        .toList(),
                    key = { it.key }
                ) { (name, versions) ->

                    val latestVersions = versions
                        .groupBy { it.groupId } // Aynı name içinde, groupId'ye göre grupla
                        .mapNotNull { (_, groupItems) ->
                            groupItems.maxByOrNull { it.startDate.year * 100 + it.startDate.month }
                        }
                    val id = latestVersions.firstOrNull()?.groupId ?: return@items
                    val isVisible = visibleItemIds[id] ?: true

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically()
                    ) {
                        RecurringItemRow(
                            name = name,
                            versions = latestVersions,
                            onEdit = { editingItem = it },
                            onDelete = { toDelete ->
                                // Animasyon tamamlandıktan sonra gerçek silme işlemi
                                CoroutineScope(Dispatchers.Main).launch {
                                    visibleItemIds[toDelete] = false
                                    delay(300) // Animasyon süresine eşit
                                    onDeleteGroup(toDelete)

                                }
                            }
                        )
                    }
                }
                item {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        IconButton(onClick = { showAddDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onClose()
            }) {
                Text(UiText.StringResourceId(Res.string.close).asString())
            }
        }
    )

    if (showAddDialog) {

        RecurringItemAddDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { item ->
                    onAddNew(item.copy(isIncome = isIncome))
                    showAddDialog = false
            },
            isIncome = isIncome
        )
    }

    editingItem?.let { item ->


        RecurringItemGroupEditDialog(
            initialItems = items.filter { it.groupId == item.groupId },
            onDismiss = { editingItem = null },
            onSaveGroup = { updatedItems ->
                onUpdateGroup(updatedItems)
                editingItem = null
            },
            onUpdateItem = { updatedItem ->
                // Güncellenen item'ı listeye ekle
                val updatedList = items.toMutableList()
                val index = updatedList.indexOfFirst { it.id == updatedItem.id }
                if (index != -1) {
                    updatedList[index] = updatedItem
                } else {
                    updatedList.add(updatedItem)
                }
                onUpdateGroup(updatedList)
            },
            onDelete = {
                onDelete.invoke(it)
            }
        )
    }
}

@Composable
fun RecurringItemRow(
    name: String,
    versions: List<RecurringFinancialItem>,
    onEdit: (RecurringFinancialItem) -> Unit,
    onDelete: (String) -> Unit
) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Text(name, style = MaterialTheme.typography.titleMedium)
        versions.sortedByDescending { it.startDate.year * 100 + it.startDate.month }.forEach { item ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${item.amount?.formatted()}")
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { onEdit(item) }) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                    }
                    IconButton(onClick = { onDelete(item.groupId) }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }
    }
}


@Composable
fun RecurringItemAddDialog(
    onDismiss: () -> Unit,
    onAdd: (RecurringFinancialItem) -> Unit,
    isIncome: Boolean
) {
    var name by remember { mutableStateOf( "") }
    var nameError by remember { mutableStateOf<UiText?>(null) }
    var amount by remember { mutableStateOf<Money?>(emptyMoney()) }
    var amountError by remember { mutableStateOf<UiText?>(null) }
    var isIncome by remember { mutableStateOf<Boolean>( isIncome) }
    var needType by remember { mutableStateOf( ExpenseNeedType.NONE) }
    var scheduledDay by remember { mutableStateOf<Int?>(null) }

    var startDate by remember { mutableStateOf<AppDate>(currentAppDate()) }


    val months = (1..12).toList()
    val years = (getCurrentYear() - 5..getCurrentYear() + 5).toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = UiText.StringResourceId(
                    if (isIncome) Res.string.add_income
                         else Res.string.add_expense
            ).asString())
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = null
                    },
                    label = { Text("Adı") },
                    isError = nameError != null,
                )
                nameError?.let {
                    Text(
                        text = it.asString(),
                        color = AppColors.error,
                        style = AppTypography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }


                MoneyInput(
                    money = amount,
                    onValueChange = { newAmount ->
                        amount = newAmount
                        amountError = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = amountError != null,
                    errorMessage = amountError
                )


                amountError?.let { errorMessage ->
                    Text(
                        text = errorMessage.asString() ,
                        color = AppColors.error,
                        style = AppTypography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }


                if (!isIncome) {
                    Row {
                        ExpenseNeedType.entries.forEach { type ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                RadioButton(
                                    selected = needType == type,
                                    onClick = { needType = type }
                                )
                                Text(type.name)
                            }
                        }
                    }
                }



                DayOfMonthSelector(
                    selectedDay = scheduledDay,
                    onDayChange = { scheduledDay = it }
                )


                // Başlangıç ve Bitiş Tarihleri
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DateSelector(
                        month = startDate.month,
                        onMonthChanged = {startDate =  startDate.copy(month = it) },
                        year = startDate.year,
                        onYearChanged = {startDate = startDate.copy(year = it) },
                        months = months,
                        years = years

                    )
                }

            }
        },
        confirmButton = {
            Button(onClick = {
                val item = RecurringFinancialItem(
                    id = 0,
                    groupId = createTimestampId(),
                    name = name,
                    amount = amount ?: emptyMoney(),
                    isIncome = isIncome,
                    needType = needType,
                    scheduledDay = scheduledDay,
                    startDate = startDate,
                )

                var isValid = false
                if (item.name.isBlank()) {
                    nameError = UiText.StringResourceId(Res.string.expense_name)
                } else if ((item.amount?.amount ?: emptyMoney().amount) <= 0) {
                    amountError = UiText.StringResourceId(Res.string.amount_must_be_greater_than_zero)
                } else {
                    isValid = true
                }
                if (isValid){
                    onAdd(item)
                    onDismiss()
                }else{
                    //handleError
                }

            }) {
                Text(UiText.StringResourceId( Res.string.save).asString())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(UiText.StringResourceId( Res.string.cancel).asString())
            }
        }
    )

}


@Preview
@Composable
fun RecurringFinancialItemDialogPreview() {
    val items = listOf(
        RecurringFinancialItem(
            id = 1,
            groupId = "3",
            name = "Kira",
            amount = Money(amount = 1000.0, currency = Currency.TRY),
            isIncome = false,
            needType = ExpenseNeedType.NEED,
            scheduledDay = 5,
            startDate = AppDate(2023, 1),
            endDate = null

        ),
        RecurringFinancialItem(
            id = 2,
            groupId = "4",
            name = "Elektrik",
            amount = Money(200.0, currency = Currency.TRY),
            isIncome = false,
            needType = ExpenseNeedType.DESIRE,
            scheduledDay = 10,
            startDate = AppDate(2023, 1),

            endDate = null
        )
    )

    RecurringFinancialItemDialog(
        title = "Gelir/Giderler",
        items = items,
        onDismiss = {},
        onClose = {},
    )
}


@Composable
fun RecurringItemGroupEditDialog(
    initialItems: List<RecurringFinancialItem>,
    onDismiss: () -> Unit,
    onSaveGroup: (List<RecurringFinancialItem>) -> Unit,
    onDelete: (RecurringFinancialItem) -> Unit = { },
    onUpdateItem: (RecurringFinancialItem) -> Unit = { },

) {
    val months = (1..12).toList()
    val years = (getCurrentYear() - 5..getCurrentYear() + 5).toList()

    var name by remember { mutableStateOf(initialItems.firstOrNull()?.name ?: "") }
    //val items = remember { mutableStateListOf<RecurringFinancialItem>().apply { addAll(initialItems) } }

    // Temporary item (her zaman gösterilir)
    var tempAmount by remember { mutableStateOf<Money?>(null) }
    var tempStartDate by remember { mutableStateOf<AppDate>(currentAppDate()) }
    var tempEndDate by remember { mutableStateOf<AppDate?>(null) }
    var validationErrors by remember { mutableStateOf<Pair<Int, UiText>?>(null) }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(
            text = UiText.StringResourceId(
                if (initialItems.firstOrNull()?.isIncome == true) Res.string.incomes
                else Res.string.expenses
            ).asString())
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 500.dp)
            ) {
                // 1. Name TextField
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = {
                            Text(
                                text = UiText.StringResourceId(
                                    if (initialItems.firstOrNull()?.isIncome == true) Res.string.income_name
                                    else Res.string.expense_name
                                ).asString()
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 2. New Item Input
                item {
                    var errorMessageNew by remember { mutableStateOf<UiText?>(null) }

                    NewRecurringItemInput(
                        amount = tempAmount,
                        errorMessage = errorMessageNew?.asString(),
                        onAmountChange = {
                            tempAmount = it
                            errorMessageNew = null
                                         },
                        startMonth = tempStartDate.month,
                        onStartMonthChange = {
                            tempStartDate = tempStartDate.copy( month = it)
                            errorMessageNew = null
                        },
                        startYear = tempStartDate.year,
                        onStartYearChange = { tempStartDate = tempStartDate.copy(year = it)
                            errorMessageNew = null
                        },
                        months = months,
                        years = years,
                        onSave = {
                            val newItem = RecurringFinancialItem(
                                id = 0,
                                name = name,
                                amount = Money(amount = tempAmount?.amount ?: 0.0, currency = Currency.TRY),
                                endDate = tempEndDate,
                                groupId = initialItems.firstOrNull()?.groupId ?: createTimestampId(),
                                isIncome = initialItems.firstOrNull()?.isIncome ?: true,
                                startDate =tempStartDate
                            )

                            val tempItems = initialItems.toMutableList()
                            tempItems.add(newItem)
                            errorMessageNew = hasDateConflict(tempItems)?.second
                            //val updatedList = items + newItem
                            //errorMessageNew = hasDateConflict(updatedList)?.second
                            if (errorMessageNew == null) {
                                onSaveGroup(adjustOpenEndedRecurringItems(tempItems))
                            }
                        }
                    )
                }

                // 3. Divider
                item {
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }

                // 4. Existing Items
                items(
                    items = initialItems.sortedByDescending { it.startDate.year * 100 + it.startDate.month },
                    key = { it.id }
                ) { item ->

                    SwipeToDeleteWrapper(
                        shape = CardDefaults.shape,
                        onDelete = {
                            onDelete(item)
                            //items.remove(item)
                            //validationErrors = null
                        },
                    ) {
                        ExistingRecurringItemCard(
                            item = item,
                            months = months,
                            years = years,
                            onUpdate = { updatedItem ->
                                onUpdateItem(updatedItem)
                                validationErrors = null
                            },
                            validationError = if (validationErrors?.first == item.id) {
                                validationErrors?.second?.asString()
                            } else null
                        )
                    }

                }

            }
        },
        confirmButton = {
            val tempItems = initialItems.toMutableList()

            Button(onClick = {
                // Eğer temp input anlamlıysa onu da ekle
                val amount = tempAmount
                if (amount != null && amount.amount > 0.0) {
                    val newItem = RecurringFinancialItem(
                        id = 0,
                        name = name,
                        amount = amount,
                        endDate = tempEndDate,
                        groupId = initialItems.firstOrNull()?.groupId ?: createTimestampId(),
                        isIncome = initialItems.firstOrNull()?.isIncome ?: true,
                        startDate = tempStartDate
                    )
                    tempItems.add(newItem)
                    //items.add(newItem)

                    // Temp alanlar resetleniyor
                    tempAmount = null
                    tempStartDate = currentAppDate()
                    tempEndDate = null
                }

                val updatedItems = tempItems.map { it.copy(name = name) }
                // Validation kontrolü
                validationErrors = hasDateConflict(updatedItems)
                if (validationErrors != null) {
                    return@Button
                }
                onSaveGroup(updatedItems)
                onDismiss()
            }) {
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


@OptIn(ExperimentalTime::class)
fun hasDateConflict(items: List<RecurringFinancialItem>): Pair<Int, UiText>?  {
    val sorted = items.sortedBy { it.startDate() }

    for (i in 0 until sorted.size - 1) {
        val current = sorted[i]
        val next = sorted[i + 1]

        val currentStart = current.startDate()
        val currentEnd = current.endDate() ?: current.startDate()
        val nextStart = next.startDate()

        // 1. Aynı startDate olamaz
        if (currentStart == nextStart){

            return Pair(
                current.id, UiText.StringResourceId(
                    id = Res.string.start_dates_cannot_be_same,
                    args = arrayOf(currentStart.month.number, currentStart.year)
                )
            )

        }


        // 2. Yeni başlangıç tarihi, eskiye göre max 1 ay sonra olabilir
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val latestAllowed = today.plus(1, DateTimeUnit.MONTH)
        if (currentStart > latestAllowed){
            return Pair(
                current.id ,  UiText.StringResourceId(
                    id = Res.string.start_date_must_be_before,
                    args = arrayOf(latestAllowed.month.number, latestAllowed.year)
                )

            )

        }
        // 3. Sonraki item'ın başlangıç tarihi, mevcut item'ın tarih aralığının içine düşemez
        if (nextStart > currentStart && nextStart <= currentEnd) {
            return Pair(
                next.id , UiText.StringResourceId(
                    id = Res.string.date_ranges_conflict,
                    args = arrayOf(
                        next.name,
                        nextStart.month.number,
                        nextStart.year,
                        current.name,
                        currentStart.month.number,
                        currentStart.year,
                        currentEnd.month.number,
                        currentEnd.year
                    )
                )
            )
        }

        // 3. current'ın endDate'i, sonraki startDate'den önce olmalı (== de dahil değil)
        if (currentEnd >= nextStart)
            return Pair(
            current.id ,  UiText.StringResourceId(
                id = Res.string.date_ranges_conflict,
                args = arrayOf(
                    current.name,
                    currentStart.month.number,
                    currentStart.year,
                    next.name,
                    nextStart.month.number,
                    nextStart.year
                )
            )
        )



    }
    for (item in sorted) {
        val start = item.startDate()
        val end = item.endDate()

        if (end != null && start > end) {
            return Pair(
                item.id , UiText.StringResourceId(
                    id = Res.string.start_date_after_end_date,
                    args = arrayOf(
                        item.name,
                        start.month.number,
                        start.year,
                        end.month.number,
                        end.year
                    )
                )
            )
        }
        if (item.endDate?.month == null && item.endDate?.year != null || item.endDate?.month != null && item.endDate?.year == null) {
            return Pair(
                item.id , UiText.StringResourceId(
                    id = Res.string.missing_month_or_year
                )
            )
        }

    }

    return null
}


@Composable
fun ExistingRecurringItemCard(
    item: RecurringFinancialItem,
    months: List<Int>,
    years: List<Int>,
    onUpdate: (RecurringFinancialItem) -> Unit,
    validationError: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (validationError != null) AppColors.error else Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            MoneyInput(
                money = item.amount,
                onValueChange = { newAmount ->
                    onUpdate(item.copy(amount = newAmount?: emptyMoney()))
                },
                modifier = Modifier.fillMaxWidth()

            )


            Row {
                DateSelector(
                    title = "Baş. Tar.",
                    month = item.startDate.month,
                    onMonthChanged = {
                        onUpdate(
                            item.copy(
                                startDate = item.startDate.copy(month = it)
                            )
                        )
                    },
                    year = item.startDate.year,
                    onYearChanged = {
                        onUpdate(
                            item.copy(
                                startDate = item.startDate.copy(year = it)
                            )
                        )
                                    },
                    months = months,
                    years = years
                )
            }

            Row {
                DateSelector(
                    title = "Bit. Tar.",
                    month = item.endDate?.month,
                    onMonthChanged = {
                        onUpdate(
                            item.copy(
                                endDate = item.endDate?.copy(month = it),
                            )
                        )
                    },
                    year = item.endDate?.year,
                    onYearChanged = { onUpdate(item.copy(
                                endDate = item.endDate?.copy(year = it)
                    )) },
                    months = months,
                    years = years
                )
            }

            validationError?.let {
                Text(
                    text = it,
                    color = AppColors.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }


        }
    }
}

@Composable
    fun NewRecurringItemInput(
    amount: Money?,
    onAmountChange: (Money?) -> Unit,
    startMonth: Int?,
    onStartMonthChange: (Int) -> Unit,
    startYear: Int?,
    onStartYearChange: (Int) -> Unit,
    months: List<Int>,
    years: List<Int>,
    onSave: () -> Unit,
    errorMessage: String? = null

) {

    var amountError by remember { mutableStateOf<UiText?>(null) }

    Column(modifier = Modifier
        .fillMaxWidth()
    ) {

        Text(UiText.StringResourceId(Res.string.new_amount).asString(), style = MaterialTheme.typography.titleMedium)


        MoneyInput(
            money = amount ?: Money(amount = 0.0, currency = Currency.TRY),
            onValueChange = { newAmount ->
                onAmountChange(newAmount)
                amountError = null
            },
            modifier = Modifier.fillMaxWidth(),
            isError = amountError != null,
            errorMessage = amountError
        )

        amountError?.asString()?.let {
            Text(
                text = it,
                color = AppColors.error,
                style = AppTypography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }


        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DropdownMenuSelector(
                months,
                startMonth,
                isNullable = false,
                onSelected = onStartMonthChange
            )
            DropdownMenuSelector(
                years,
                startYear,
                isNullable = false,
                onSelected = onStartYearChange
            )
        }

        Spacer(Modifier.height(8.dp))

        if (!errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(Modifier.height(8.dp))

        }

        Button(
            onClick = {
                if ((amount?.amount ?: 0.0) <= 0.0){
                    amountError = UiText.StringResourceId(Res.string.amount_must_be_greater_than_zero)
                }else{ // SUCCESS CASE
                    onSave()

                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(UiText.StringResourceId(Res.string.update).asString())
        }

    }
}
fun adjustOpenEndedRecurringItems(items: List<RecurringFinancialItem>): List<RecurringFinancialItem> {
    val sorted = items.sortedBy { it.startDate() }

    return sorted.mapIndexed { index, current ->
        if (current.endDate() == null && index < sorted.lastIndex) {
            val next = sorted[index + 1]
            val adjustedEndDate = next.startDate().minus(DatePeriod(months = 1))
            current.copy(
                endDate = AppDate(month = adjustedEndDate.month.number, year = adjustedEndDate.year, day = current.endDate?.day),
            )
        } else {
            current
        }
    }
}

