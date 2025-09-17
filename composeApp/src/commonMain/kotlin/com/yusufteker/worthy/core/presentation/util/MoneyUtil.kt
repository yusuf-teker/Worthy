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



class MoneyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text
        val locale = Locale.current

        // Türkçe için virgül, diğer diller için nokta kullan
        val transformed = if (locale.language.lowercase() == "tr") {
            input.replace('.', ',')
        } else {
            input
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

    val isNegative = amount < 0
    val absAmount = kotlin.math.abs(amount)

    val integerPart = floor(absAmount).toLong()
    val fractionalPart = ((absAmount - integerPart) * 100).roundToInt()

    // Binlik ayırıcı ekleme
    val integerStr = integerPart.toString()
        .reversed()
        .chunked(3)
        .joinToString(thousandSeparator)
        .reversed()

    val formatted = "$integerStr$decimalSeparator${fractionalPart.toString().padStart(2, '0')} ${currency.symbol}"

    return if (isNegative) "-$formatted" else formatted
}



fun Money.formattedShort(): String {
    val locale: Locale = Locale.current
    val integerPart = floor(amount).toLong()

    return when (locale.language.lowercase()) {

        "tr" -> when {
            integerPart >= 1_000_000_000_000 -> {
                val trillions = integerPart / 1_000_000_000_000
                val billions = (integerPart % 1_000_000_000_000) / 1_000_000_000
                "${currency.symbol} ${trillions} Tr${if (billions > 0) " ${billions} Mr" else ""}".trim()
            }
            integerPart >= 1_000_000_000 -> {
                val billions = integerPart / 1_000_000_000
                val millions = (integerPart % 1_000_000_000) / 1_000_000
                "${currency.symbol} ${billions} Mr${if (millions > 0) " ${millions} Mn" else ""}".trim()
            }
            integerPart >= 1_000_000 -> {
                val millions = integerPart / 1_000_000
                val thousands = (integerPart % 1_000_000) / 1_000
                "${currency.symbol} ${millions} Mn${if (thousands > 0) " ${thousands} B" else ""}".trim()
            }
            integerPart >= 1_000 -> {
                val thousands = integerPart / 1_000
                "${currency.symbol} ${thousands} B"
            }
            else -> "${currency.symbol} $integerPart"
        }

        else -> when {
            integerPart >= 1_000_000_000_000 -> {
                val trillions = integerPart / 1_000_000_000_000
                val billions = (integerPart % 1_000_000_000_000) / 1_000_000_000
                "${currency.symbol} ${trillions}T${if (billions > 0) " ${billions}B" else ""}".trim()
            }
            integerPart >= 1_000_000_000 -> {
                val billions = integerPart / 1_000_000_000
                val millions = (integerPart % 1_000_000_000) / 1_000_000
                "${currency.symbol} ${billions}B${if (millions > 0) " ${millions}M" else ""}".trim()
            }
            integerPart >= 1_000_000 -> {
                val millions = integerPart / 1_000_000
                val thousands = (integerPart % 1_000_000) / 1_000
                "${currency.symbol} ${millions}M${if (thousands > 0) " ${thousands}K" else ""}".trim()
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
    if (this.isEmpty()) return emptyMoney()
    return Money(this.sumOf { it.amount }, this.first().currency)
}


suspend fun List<Money>.sumWithCurrencyConverted(
    currencyConverter: CurrencyConverter, currency: Currency
): Money {
    if (this.isEmpty()) return emptyMoney(currency)
    currencyConverter.convertAll(this, currency).sumWithoutCurrencyConverted()
    return currencyConverter.convertAll(this, currency).sumWithoutCurrencyConverted()

}

