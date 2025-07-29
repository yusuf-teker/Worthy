package com.yusufteker.worthy.core.data.service.datasource

import com.yusufteker.worthy.core.data.service.model.CachedRates
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesCacheDataSource

class CurrencyRatesCacheDataSourceImpl : CurrencyRatesCacheDataSource {

    private val cacheMap = mutableMapOf<Currency, CachedRates>()

    override suspend fun getRates(base: Currency): CachedRates? {
        return cacheMap[base]
    }

    override suspend fun saveRates(cachedRates: CachedRates) {
        cacheMap[cachedRates.base] = cachedRates
    }
}