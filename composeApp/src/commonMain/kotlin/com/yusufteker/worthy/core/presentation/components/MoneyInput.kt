package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.util.MoneyVisualTransformation
import com.yusufteker.worthy.core.presentation.util.emptyMoney
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.amount
import kotlin.math.roundToInt

// MoneyInput.kt - Düzeltilmiş versiyon

@Composable
fun MoneyInput(
    money: Money? = emptyMoney(),
    onValueChange: (Money?) -> Unit,
    modifier: Modifier = Modifier,
    label: UiText = UiText.StringResourceId(Res.string.amount),
    isError: Boolean = false,
    errorMessage: UiText? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var textValue by remember(money) {
        mutableStateOf(
            if (money?.amount == 0.0) "" else formatDoubleForInput(money?.amount ?: 0.0)
        )
    }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        OutlinedTextField(
            value = textValue,
            placeholder = { Text(label.asString()) },
            onValueChange = { input ->
                // Maksimum karakter sınırı (milyar seviyesine kadar)
                if (input.length > 15) return@OutlinedTextField

                // Sadece rakam, nokta ve virgül kabul et
                if (!input.all { it.isDigit() || it == '.' || it == ',' }) {
                    return@OutlinedTextField
                }

                // Birden fazla ayırıcı kontrolü
                val dotCount = input.count { it == '.' }
                val commaCount = input.count { it == ',' }
                if (dotCount + commaCount > 1) return@OutlinedTextField

                // Küsurat basamak kontrolü
                val separatorIndex = maxOf(input.indexOf('.'), input.indexOf(','))
                if (separatorIndex >= 0 && input.length - separatorIndex > 3) {
                    return@OutlinedTextField
                }

                textValue = input

                // Double'a çevirme işlemi
                val normalizedInput = input.replace(',', '.')
                val doubleValue = parseDoubleCarefully(normalizedInput)

                if (doubleValue != null) {
                    onValueChange(money?.copy(amount = doubleValue))
                } else if (input.isEmpty()) {
                    onValueChange(money?.copy(amount = 0.0))
                }
            },
            visualTransformation = MoneyVisualTransformation(),
            label = { Text(label.asString()) },
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }),
            modifier = modifier.fillMaxWidth(),
            trailingIcon = {
                Box {
                    TextButton(
                        onClick = { expanded = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        money?.currency?.symbol?.let { Text(it) }
                    }

                    DropdownMenu(
                        expanded = expanded, onDismissRequest = { expanded = false }) {
                        Currency.entries.forEach { currency ->
                            DropdownMenuItem(text = { Text("${currency.symbol} ") }, onClick = {
                                expanded = false
                                onValueChange(money?.copy(currency = currency))
                            })
                        }
                    }
                }
            },
            singleLine = true
        )

        errorMessage?.let {
            MessageText(UiMessage.Error(it.asString()))
        }

        // Debug için gerçek değeri göster
        MessageText(UiMessage.Info("Gerçek değer: ${money?.amount}"))
    }
}

// Yardımcı fonksiyonlar

fun parseDoubleCarefully(input: String): Double? {
    if (input.isEmpty() || input == "." || input == ",") return null

    return try {
        val value = input.toDouble()
        // Makul sınırlar içinde mi kontrol et
        if (value > Double.MAX_VALUE / 2 || value < 0) null
        else {
            // 2 ondalık basamağa yuvarla
            kotlin.math.round(value * 100.0) / 100.0
        }
    } catch (e: NumberFormatException) {
        null
    }
}

fun formatDoubleForInput(value: Double): String {
    return if (value == kotlin.math.floor(value) && value < 1e10) {
        // Tam sayı ise .0 gösterme
        value.toLong().toString()
    } else {
        // Ondalıklı sayı ise 2 basamakla sınırla
        val rounded = kotlin.math.round(value * 100.0) / 100.0
        val intPart = kotlin.math.floor(rounded).toLong()
        val fracPart = ((rounded - intPart) * 100).roundToInt()

        if (fracPart == 0) {
            intPart.toString()
        } else if (fracPart % 10 == 0) {
            "$intPart.${fracPart / 10}"
        } else {
            "$intPart.${fracPart.toString().padStart(2, '0')}"
        }
    }
}

