package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import kotlinx.serialization.Serializable
import kotlin.math.pow

@Serializable
data class Money(
    val amount: Double = 0.0,
    val currency: Currency
) {
    fun formatted(): String {
        return "${currency.symbol} ${amount.toFixedSafe(2)}"
    }
    fun setAmount(newAmount: Double): Money {
        return Money(newAmount, currency)
    }
    fun Double.toFormattedWithThousandsSeparator(
        digits: Int = 2,
        separator: Char = '.'
    ): String {
        val multiplier = 10.0.pow(digits)
        val rounded = kotlin.math.round(this * multiplier) / multiplier
        val parts = rounded.toString().split(".")

        val integerPart = parts[0]
        val decimalPart = parts.getOrNull(1) ?: ""

        // Tam sayı kısmını tersine çevir, 3'erli grupla, sonra tekrar ters çevir
        val formattedInt = integerPart.reversed()
            .chunked(3)
            .joinToString(separator.toString())
            .reversed()

        // Ondalık kısmı tamamla
        val formattedDecimal = decimalPart.padEnd(digits, '0')

        return "$formattedInt.$formattedDecimal"
    }
}

fun emptyMoney(currency: Currency = Currency.TRY) = Money(0.0, currency)


fun List<Money>.sumWithoutCurrencyConverted(): Money{
    return Money(this.sumOf { it.amount }, this.first().currency)
}

suspend fun List<Money>.sumWithCurrencyConverted(currencyConverter: CurrencyConverter, currency: Currency): Money{
    if (this.isEmpty())
        return emptyMoney( currency)
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

