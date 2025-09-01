package com.yusufteker.worthy.core.domain.repository

import com.yusufteker.worthy.core.domain.DataError
import com.yusufteker.worthy.core.domain.Result
import com.yusufteker.worthy.core.domain.model.Currency

interface CurrencyRatesRepository {

    suspend fun getRates(base: Currency): Result<Map<Currency, Double>, DataError.Remote>
}
