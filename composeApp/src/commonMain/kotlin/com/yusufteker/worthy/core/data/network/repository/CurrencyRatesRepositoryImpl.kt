package com.yusufteker.worthy.core.data.network.repository

import com.yusufteker.worthy.core.data.network.util.NetworkStatus
import com.yusufteker.worthy.core.domain.DataError
import com.yusufteker.worthy.core.domain.Result
import com.yusufteker.worthy.core.domain.model.CachedExchangeRate
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.repository.CurrencyRatesRepository
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesCacheDataSource
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesRemoteDataSource
import io.github.aakira.napier.Napier
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

//Repository = data kaynaklarını birleştirme
class CurrencyRatesRepositoryImpl(
    private val remote: CurrencyRatesRemoteDataSource,
    private val cache: CurrencyRatesCacheDataSource, // DataStore, Room veya SharedPreferences tabanlı
    private val networkStatus: NetworkStatus
) : CurrencyRatesRepository {

    @OptIn(ExperimentalTime::class)
    override suspend fun getRates(base: Currency): Result<Map<Currency, Double>, DataError.Remote> {
        val cached = cache.getRates(base)
        val now = Clock.System.now().toEpochMilliseconds()

        if (cached != null && now - cached.timestampMillis < ONE_DAY_MS) {
            Napier.d("Repository: Returning rates from CACHE for base $base")
            return Result.Success(cached.rates)
        }



        if (!networkStatus.isOnline()) {
            Napier.w("Repository: No internet connection, returning cached rates if available")
            return if (cached != null) {
                Result.Success(cached.rates)
            } else {
                Result.Error(DataError.Remote.NO_INTERNET)
            }
        }
        Napier.d("Repository: Fetching rates from NETWORK for base $base")
        return when (val ratesResult = remote.fetchRates(base)) {
            is Result.Success -> {
                cache.saveRates(
                    CachedExchangeRate(
                        base = base,
                        rates = ratesResult.data,
                        timestampMillis = now
                    )
                )
                Result.Success(ratesResult.data)
            }
            is Result.Error -> {
                // fallback: eğer cache varsa onu dön
                if (cached != null) {
                    Napier.w("Repository: Using STALE cache after network failure", tag = "CurrencyRatesRepository")
                    Result.Success(cached.rates)
                } else {
                    Result.Error(ratesResult.error)
                }
            }
        }
    }

    companion object {
        private const val ONE_DAY_MS = 24 * 60 * 60 * 1000
    }
}
