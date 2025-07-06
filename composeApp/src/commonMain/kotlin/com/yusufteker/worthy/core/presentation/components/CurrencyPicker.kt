package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yusufteker.worthy.core.presentation.UiText
import org.jetbrains.compose.ui.tooling.preview.Preview
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.label_currency

@Composable
fun CurrencyPicker2(
    selectedCurrencyCode: String,
    onCurrencySelected: (String) -> Unit,
    currencySymbols: Map<String, String> = mapOf(
        "TRY" to "₺",
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "JPY" to "¥"
    )
) {
    var expanded by remember { mutableStateOf(false) }

    // Ekranda göstereceğimiz metin (kod + sembol)
    val displayText = "${currencySymbols[selectedCurrencyCode] ?: ""}  $selectedCurrencyCode"

    Box {
        OutlinedTextField(
            value = displayText,
            onValueChange = { /* klavyeden giriş kapalı */ },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null, // Ripple efektini kaldırır
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    expanded = true
                },
            readOnly = true,
            label = { Text("Para Birimi") },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            currencySymbols.forEach { (code, symbol) ->
                DropdownMenuItem(
                    text = { Text("$symbol  $code") },
                    onClick = {
                        onCurrencySelected(code)
                        expanded = false
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPicker3(
    selectedCurrencyCode: String,
    onCurrencySelected: (String) -> Unit,
    currencySymbols: Map<String, String> = mapOf(
        "TRY" to "₺",
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "JPY" to "¥"
    )
) {
    var expanded by remember { mutableStateOf(false) }

    val displayText = "${currencySymbols[selectedCurrencyCode] ?: ""}  $selectedCurrencyCode"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }            // ⇠ Alanın neresine tıklanırsa tıklansın
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Para Birimi") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, false)                                  // ⇠ Menü pozisyonu için zorunlu
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencySymbols.forEach { (code, symbol) ->
                DropdownMenuItem(
                    text = { Text("$symbol  $code") },
                    onClick = {
                        onCurrencySelected(code)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPicker(
    modifier: Modifier = Modifier,
    selectedCurrencyCode: String,
    onCurrencySelected: (String) -> Unit,
    currencySymbols: Map<String, String> = mapOf(
        "TRY" to "₺", "USD" to "$", "EUR" to "€", "GBP" to "£", "JPY" to "¥"
    )
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = "${currencySymbols[selectedCurrencyCode] ?: ""}  $selectedCurrencyCode"

    ExposedDropdownMenuBox(
        modifier = modifier.wrapContentWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},            // readOnly
            readOnly = true,
            label = { Text(UiText.StringResourceId(id = Res.string.label_currency).asString()) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)   // 🔑 yeni imza
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencySymbols.forEach { (code, symbol) ->
                DropdownMenuItem(
                    text = { Text("$symbol  $code") },
                    onClick = {
                        onCurrencySelected(code)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun CurrencyPickerPreview() {
    CurrencyPicker(
        selectedCurrencyCode = "USD",
        onCurrencySelected = {}
    )
}