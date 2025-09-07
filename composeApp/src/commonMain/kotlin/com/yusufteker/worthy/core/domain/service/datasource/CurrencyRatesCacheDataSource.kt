package com.yusufteker.worthy.core.domain.service.datasource

import com.yusufteker.worthy.core.domain.model.CachedExchangeRate
import com.yusufteker.worthy.core.domain.model.Currency

interface CurrencyRatesCacheDataSource {
    suspend fun saveRates(cachedRates: CachedExchangeRate)
    suspend fun getRates(base: Currency): CachedExchangeRate?


}