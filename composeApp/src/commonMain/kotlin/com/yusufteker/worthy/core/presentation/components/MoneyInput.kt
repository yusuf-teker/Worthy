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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.formatTwoDecimals
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
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
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
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Currency.entries.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text("${currency.symbol} ") },
                                onClick = {
                                    expanded = false
                                    onValueChange(money?.copy(currency = currency))
                                }
                            )
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

/**
 * Double değeri güvenli bir şekilde parse eder
 */
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

/**
 * Double değeri input için formatlar
 */
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
/**
 * Geliştirilmiş MoneyVisualTransformation
 */


/**
 * Geliştirilmiş para formatlaması
 */
fun Money.formatted(): String {
    val locale = Locale.current
    val decimalSeparator = if (locale.language.lowercase() == "tr") "," else "."
    val thousandSeparator = if (locale.language.lowercase() == "tr") "." else ","

    // Güvenli matematiksel işlemler
    val safeAmount = kotlin.math.max(0.0, kotlin.math.min(amount, 999_999_999_999.99))
    val integerPart = kotlin.math.floor(safeAmount).toLong()
    val fractionalPart = ((safeAmount - integerPart) * 100).roundToInt()

    // Binlik ayırıcı ekleme
    val integerStr = addThousandSeparators(integerPart.toString(), thousandSeparator)

    return "${currency.symbol} $integerStr$decimalSeparator${fractionalPart.toString().padStart(2,'0')}"
}

/**
 * Binlik ayırıcı ekleme fonksiyonu
 */
fun addThousandSeparators(number: String, separator: String): String {
    return number.reversed().chunked(3).joinToString(separator).reversed()
}

/**
 * Double uzantı fonksiyonu - güvenli para formatlaması
 */
fun Double.formatMoneyTextSafe(currency: Currency? = null, showDecimals: Boolean = true): String {
    val locale = Locale.current
    val decimalSeparator = if (locale.language.lowercase() == "tr") "," else "."
    val thousandSeparator = if (locale.language.lowercase() == "tr") "." else ","

    // Güvenli sınırlar
    val safeValue = kotlin.math.max(0.0, kotlin.math.min(this, 999_999_999_999.99))
    val integerPart = kotlin.math.floor(safeValue).toLong()
    val integerStr = addThousandSeparators(integerPart.toString(), thousandSeparator)

    val currencySymbol = currency?.symbol?.let { " $it" } ?: ""

    return if (showDecimals) {
        val fractionalPart = ((safeValue - integerPart) * 100).roundToInt()
        "$integerStr$decimalSeparator${fractionalPart.toString().padStart(2, '0')}$currencySymbol"
    } else {
        "$integerStr$currencySymbol"
    }
}

/**
 * Güvenli ondalık basamak sayma
 */
fun countDecimalPlacesLocalized(value: String): Int {
    val separatorIndex = maxOf(value.indexOf('.'), value.indexOf(','))
    return if (separatorIndex < 0) 0
    else kotlin.math.min(value.length - separatorIndex - 1, 2) // Maksimum 2 basamak
}
fun isThirdOrFourthFromEndDotOrComma(s: String): Boolean {
    if (s.length < 4) return false
    val second = s[s.length - 2]
    val third = s[s.length - 3]
    val fourth = s[s.length - 4]
    return third == '.' || third == ',' || fourth == '.' || fourth == ',' || second == '.' || second == ','
}
