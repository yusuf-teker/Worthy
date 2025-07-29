package com.yusufteker.worthy.core.domain.model

import kotlinx.serialization.Serializable
import kotlin.math.pow

@Serializable
data class Money(
    val amount: Double,
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

