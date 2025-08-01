package com.yusufteker.worthy.core.presentation.theme

object Constants {
    const val WEEKLY_MAX_HOURS = 84
    val currencies = listOf("TRY", "USD", "EUR", "GBP", "JPY")
    val currencySymbols = mapOf( // todo bunlar silinecek
        "TRY" to "₺",
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "JPY" to "¥",
    ).withDefault { "₺" }

}