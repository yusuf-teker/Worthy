package com.yusufteker.worthy.core.domain.service.datasource

import com.yusufteker.worthy.core.data.service.model.CachedRates
import com.yusufteker.worthy.core.domain.model.Currency

interface CurrencyRatesCacheDataSource {
    suspend fun saveRates(cachedRates: CachedRates)
    suspend fun getRates(base: Currency): CachedRates?
}