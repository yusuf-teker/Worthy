package com.yusufteker.worthy.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CachedExchangeRate( // USER PREFERENCE İÇERİSİNDE TUTULUYOR
    val base: Currency, val rates: Map<Currency, Double>, val timestampMillis: Long
)