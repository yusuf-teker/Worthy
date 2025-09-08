package com.yusufteker.worthy.core.domain.model

import androidx.compose.ui.text.intl.Locale
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import kotlinx.serialization.Serializable
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

@Serializable
data class Money(
    val amount: Double = 0.0, val currency: Currency
)
