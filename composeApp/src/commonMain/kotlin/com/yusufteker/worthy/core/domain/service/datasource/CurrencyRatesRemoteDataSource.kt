package com.yusufteker.worthy.core.domain.service.datasource

import com.yusufteker.worthy.core.domain.model.Currency

interface CurrencyRatesRemoteDataSource {
    suspend fun fetchRates(base: Currency): Map<Currency, Double>
}