package com.yusufteker.worthy.core.data.service.model

import com.yusufteker.worthy.core.domain.model.Currency

data class CachedRates(
    val base: Currency, val rates: Map<Currency, Double>, val timestampMillis: Long
)