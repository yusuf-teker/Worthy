package com.yusufteker.worthy.core.presentation.util

import androidx.compose.ui.text.intl.Locale

fun formatPercentSuffix(value: Int): String {
    val locale = Locale.current
    if (!locale.region.equals("TR", ignoreCase = true)) {
        return value.toString()
    }
    val str = value.toString()

    // Özel durumlar: 10, 20, 30, 40, 50, 60, 70, 80, 90
    val lastTwo = if (value >= 10) value % 100 else value
    val suffix = when (lastTwo) {
        40, 60, 90 -> "’ına"
        10,30-> "’una"
        70, 80 -> "’ine"
        20, 50 -> "’sine"
        else -> {
            val lastDigit = value % 10
            when (lastDigit) {
                1, 5,  8 -> "’ine"
                2, 7, -> "’sine"
                6 -> "’sına"
                3, 4 -> "’üne"
                9, 0 -> "’una"
                else -> "’ine"
            }
        }
    }
    return "%$str$suffix"
}

fun formatPercentSuffix(valueStr: String): String {
    val locale = Locale.current
    if (!locale.region.equals("TR", ignoreCase = true)) {
        return valueStr
    }

    // Stringi Double’a çevir
    val value = valueStr.replace(',', '.').toDoubleOrNull() ?: return valueStr

    // Ondalık kısmı al
    val parts = value.toString().split(".")
    val intPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1].padEnd(2, '0').take(2) else "00"

    // lastTwo = son iki basamak
    val lastTwo = decimalPart.toIntOrNull() ?: 0
    val suffix = when (lastTwo) {
        40, 60, 90 -> "’ına"
        10, 30 -> "’una"
        70, 80 -> "’ine"
        20, 50 -> "’sine"
        else -> {
            val lastDigit = lastTwo % 10
            when (lastDigit) {
                1, 5, 8 -> "’ine"
                2, 7 -> "’sine"
                6 -> "’sına"
                3, 4 -> "’üne"
                9, 0 -> "’una"
                else -> "’ine"
            }
        }
    }

    // formattedValue = Türkçe virgül ile 2 ondalık
    val formattedValue = "$intPart,$decimalPart"

    return "%$formattedValue$suffix"
}