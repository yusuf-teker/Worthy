package com.yusufteker.worthy.core.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.Locale
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt


class MoneyVisualTransformation() : VisualTransformation {
    val locale: Locale = Locale.current
    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text
        val transformed = if (locale.language.lowercase() == "tr") {
            input.replace('.', ',') // uzunluk değişmiyor
        } else {
            input
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset + 1
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 0 -> 0
                    offset == 1 -> 0
                    else -> offset - 1
                }
            }
        }

        return TransformedText(
            AnnotatedString(transformed),
            OffsetMapping.Identity
        )
    }
}

fun Money.formatted(): String {
    val locale: Locale = Locale.current
    val decimalSeparator = if (locale.language.lowercase() == "tr") "," else "."
    val thousandSeparator = if (locale.language.lowercase() == "tr") "." else ","

    val integerPart = floor(amount).toLong()
    val fractionalPart = ((amount - integerPart) * 100).roundToInt()

    // Binlik ayırıcı ekleme
    val integerStr = integerPart.toString().reversed().chunked(3).joinToString(thousandSeparator).reversed()

    return "${currency.symbol} $integerStr$decimalSeparator${fractionalPart.toString().padStart(2,'0')}"
}


fun Money.formattedShort(): String {
    val locale: Locale = Locale.current
    val integerPart = floor(amount).toLong()

    return when (locale.language.lowercase()) {

        "tr" -> when {
            integerPart >= 1_000_000 -> {
                val millions = integerPart / 1_000_000
                val thousands = (integerPart % 1_000_000) / 1_000
                "${currency.symbol} ${millions} Mn ${if (thousands > 0) "${thousands} B" else ""}".trim()
            }
            integerPart >= 1_000 -> {
                val thousands = integerPart / 1_000
                "${currency.symbol} ${thousands} B"
            }
            else -> "${currency.symbol} $integerPart"
        }

        else -> when {
                integerPart >= 1_000_000 -> {
                    val millions = integerPart / 1_000_000
                    val thousands = (integerPart % 1_000_000) / 1_000
                    "${currency.symbol} ${millions}M ${if (thousands > 0) "${thousands}K" else ""}".trim()
                }
                integerPart >= 1_000 -> {
                    val thousands = integerPart / 1_000
                    val remainder = (integerPart % 1_000) / 100
                    if (remainder > 0) "${currency.symbol} ${thousands}.${remainder}K"
                    else "${currency.symbol} ${thousands}K"
                }
                else -> "${currency.symbol} $integerPart"
            }

    }
}

fun Double.formatMoneyText(currency: Currency? = null, showDecimals: Boolean = true): String {
    val locale: Locale = Locale.current

    val decimalSeparator = if (locale.language.lowercase() == "tr") "," else "."
    val thousandSeparator = if (locale.language.lowercase() == "tr") "." else ","

    val integerPart = floor(this).toLong()
    val integerStr = integerPart.toString().reversed().chunked(3).joinToString(thousandSeparator).reversed()

    val currencySymbol = currency?.symbol?.let { " $it" } ?: ""
    return if (showDecimals) {
        val fractionalPart = ((this - integerPart) * 100).roundToInt()
        "$integerStr$decimalSeparator${fractionalPart.toString().padStart(2, '0')}$currencySymbol"
    } else {
        "$integerStr$currencySymbol"
    }
}


fun emptyMoney(currency: Currency = Currency.TRY) = Money(0.0, currency)

fun List<Money>.sumWithoutCurrencyConverted(): Money {
    return Money(this.sumOf { it.amount }, this.first().currency)
}


suspend fun List<Money>.sumWithCurrencyConverted(
    currencyConverter: CurrencyConverter, currency: Currency
): Money {
    if (this.isEmpty()) return emptyMoney(currency)
    currencyConverter.convertAll(this, currency).sumWithoutCurrencyConverted()
    return currencyConverter.convertAll(this, currency).sumWithoutCurrencyConverted()

}

fun Double.toFixedSafe(digits: Int): String {
    val multiplier = 10.0.pow(digits)
    val rounded = kotlin.math.round(this * multiplier) / multiplier
    val str = rounded.toString()

    return if (!str.contains(".")) {
        str + "." + "0".repeat(digits)
    } else {
        val parts = str.split(".")
        val decimal = parts.getOrNull(1) ?: ""
        parts[0] + "." + decimal.padEnd(digits, '0')
    }
}


