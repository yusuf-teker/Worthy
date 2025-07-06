package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.theme.Constants.currencySymbols
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add
import worthy.composeapp.generated.resources.add_new
import worthy.composeapp.generated.resources.amount_with_currency
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.label_amount
import worthy.composeapp.generated.resources.label_name
import worthy.composeapp.generated.resources.save
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
public fun FinancialItemDialog(
    title: String,
    items: List<ItemForDialog>,
    onDismiss: () -> Unit,
    onSave: (List<ItemForDialog>) -> Unit,
    currencyCode: String = currencySymbols.values.first(),

    ) {
    var currentItems by remember { mutableStateOf(items) }
    var showAddDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(300.dp)
            ) {
                items(currentItems) { item ->
                    FinancialItemRow(
                        item = item,
                        onDelete = {
                            currentItems = currentItems.filter { it.id != item.id }
                        },
                        onEdit = { updatedItem ->
                            currentItems = currentItems.map {
                                if (it.id == item.id) updatedItem else it
                            }
                        },
                        currencySymbol = currencySymbols.getValue(currencyCode)
                    )
                }
                item {
                    Button(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(UiText.StringResourceId(Res.string.add).asString())

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
            onAdd = { name, amount ->
                currentItems = currentItems + ItemForDialog(
                    id = Clock.System.now().toEpochMilliseconds().toString(),
                    name = name,
                    amount = amount
                )
                showAddDialog = false
            },
            currency = currencyCode

        )
    }
}

@Composable
private fun FinancialItemRow(
    item: ItemForDialog,
    onDelete: () -> Unit,
    onEdit: (ItemForDialog) -> Unit,
    currencySymbol: String = currencySymbols.values.first()
) {
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing) {
        var editName by remember { mutableStateOf(item.name) }
        var editAmount by remember { mutableStateOf(item.amount.toString()) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = editName,
                onValueChange = { editName = it },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = editAmount,
                onValueChange = { editAmount = it },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            IconButton(
                onClick = {
                    val amount = editAmount.toFloatOrNull() ?: 0f
                    onEdit(item.copy(name = editName, amount = amount))
                    isEditing = false
                }
            ) {
                Text("✓")
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = AppTypography.bodyMedium)
                Text(
                    text = UiText.StringResourceId(id = Res.string.amount_with_currency, arrayOf(item.amount.toInt(), currencySymbol)).asString(),
                    style = AppTypography.bodySmall)
            }
            Row {
                IconButton(onClick = { isEditing = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Düzenle")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Sil")
                }
            }
        }
    }
}

@Composable
private fun AddItemDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Float) -> Unit,
    currency: String = currencySymbols.values.first(),
    nameLabel: String = UiText.StringResourceId(Res.string.label_name).asString(),
    amountLabel: String = UiText.StringResourceId(Res.string.label_amount, arrayOf(currency)).asString()

) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

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
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(amountLabel) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountFloat = amount.toFloatOrNull() ?: 0f
                    if (name.isNotBlank() && amountFloat > 0) {
                        onAdd(name, amountFloat)
                    }
                }
            ) {
                Text("Ekle")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}

// Dialog için ortak data class
data class ItemForDialog(
    val id: String,
    val name: String,
    val amount: Float,
    val currency: String = currencySymbols.values.first()
)


