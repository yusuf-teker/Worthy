package com.yusufteker.worthy.core.presentation.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.screen.dashboard.domain.Category
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.category_education
import worthy.composeapp.generated.resources.category_entertainment
import worthy.composeapp.generated.resources.category_food
import worthy.composeapp.generated.resources.category_health
import worthy.composeapp.generated.resources.category_other
import worthy.composeapp.generated.resources.category_shopping
import worthy.composeapp.generated.resources.category_transport

object Constants {
    const val WEEKLY_MAX_HOURS = 84
    val currencies = listOf("TRY", "USD", "EUR", "GBP", "JPY")
    val currencySymbols = mapOf(
        "TRY" to "₺",
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "JPY" to "¥",
    ).withDefault { "₺" }

    val categories = listOf(
        Category(UiText.StringResourceId(Res.string.category_food), Icons.Default.ShoppingCart),
        Category(UiText.StringResourceId(Res.string.category_transport), Icons.Default.ShoppingCart),
        Category(UiText.StringResourceId(Res.string.category_shopping), Icons.Default.ShoppingCart),
        Category(UiText.StringResourceId(Res.string.category_entertainment), Icons.Default.ShoppingCart),
        Category(UiText.StringResourceId(Res.string.category_health), Icons.Default.ShoppingCart),
        Category(UiText.StringResourceId(Res.string.category_education), Icons.Default.ShoppingCart),
        Category(UiText.StringResourceId(Res.string.category_other), Icons.Default.ShoppingCart)
    )
}