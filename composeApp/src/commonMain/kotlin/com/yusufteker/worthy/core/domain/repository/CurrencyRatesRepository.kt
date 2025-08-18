package com.yusufteker.worthy.core.domain.repository

import com.yusufteker.worthy.core.domain.model.Currency

interface CurrencyRatesRepository {

    suspend fun getRates(base: Currency): Map<Currency, Double>
}
