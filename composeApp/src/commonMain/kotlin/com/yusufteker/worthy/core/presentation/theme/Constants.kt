package com.yusufteker.worthy.core.presentation.theme
import androidx.compose.ui.unit.dp

object Constants {
    const val WEEKLY_MAX_HOURS = 84
    const val MAX_INSTALLMENT_COUNT = 36

    val fabIconSize = 48.dp
    val bottomNavItemIconSize = 24.dp

    val currencies = listOf("TRY", "USD", "EUR", "GBP", "JPY")
    val currencySymbols = mapOf( // todo bunlar silinecek
        "TRY" to "₺",
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "JPY" to "¥",
    ).withDefault { "₺" }

}