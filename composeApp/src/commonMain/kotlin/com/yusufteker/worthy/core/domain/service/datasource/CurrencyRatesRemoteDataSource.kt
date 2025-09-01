package com.yusufteker.worthy.core.domain.service.datasource

import com.yusufteker.worthy.core.domain.DataError
import com.yusufteker.worthy.core.domain.Result
import com.yusufteker.worthy.core.domain.model.Currency

interface CurrencyRatesRemoteDataSource {
    suspend fun fetchRates(base: Currency): Result<Map<Currency, Double>, DataError.Remote>
}
