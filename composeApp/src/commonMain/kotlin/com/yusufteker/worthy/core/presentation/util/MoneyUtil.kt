package com.yusufteker.worthy.core.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.Locale
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt

fun formatMoney(amount: Double?, locale: androidx.compose.ui.text.intl.Locale): String {
    val decimalSeparator = if (locale.language.lowercase() == "tr") "," else "."
    val integerPart = floor(amount?:0.0).toLong()
    val fractionalPart = round(( (amount?:0.0) - integerPart) * 100).toInt()
    return "$integerPart$decimalSeparator${fractionalPart.toString().padStart(2, '0')}"
}



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

fun Double.formatMoney(currency: Currency): String{
    val locale: Locale = Locale.current
    val decimalSeparator = if (locale.language.lowercase() == "tr") "," else "."
    val thousandSeparator = if (locale.language.lowercase() == "tr") "." else ","

    val integerPart = floor(this).toLong()
    val fractionalPart = ((this - integerPart) * 100).roundToInt()

    // Binlik ayırıcı ekleme
    val integerStr = integerPart.toString().reversed().chunked(3).joinToString(thousandSeparator).reversed()

    return "${currency.symbol} $integerStr$decimalSeparator${fractionalPart.toString().padStart(2,'0')}"
}
fun Money.formattedWithoutDecimals(): String {
    val locale: Locale = Locale.current
    val thousandSeparator = if (locale.language.lowercase() == "tr") "." else ","

    val integerPart = floor(amount).toLong()

    val integerStr = integerPart.toString()
        .reversed()
        .chunked(3)
        .joinToString(thousandSeparator)
        .reversed()

    return "${currency.symbol} $integerStr"
}


fun Double.formatMoneyText(): String {
    val locale: Locale = Locale.current
    val decimalSeparator = if (locale.language.lowercase() == "tr") "," else "."
    val thousandSeparator = if (locale.language.lowercase() == "tr") "." else ","

    val integerPart = floor(this).toLong()
    val fractionalPart = ((this - integerPart) * 100).roundToInt()

    val integerStr = integerPart.toString().reversed().chunked(3).joinToString(thousandSeparator).reversed()

    return "$integerStr$decimalSeparator${fractionalPart.toString().padStart(2,'0')}"
}
fun Int.formatMoneyText(): String {
    val locale: Locale = Locale.current
    val thousandSeparator = if (locale.language.lowercase() == "tr") "." else ","

    val integerStr = this.toString().reversed().chunked(3).joinToString(thousandSeparator).reversed()
    return integerStr
}

