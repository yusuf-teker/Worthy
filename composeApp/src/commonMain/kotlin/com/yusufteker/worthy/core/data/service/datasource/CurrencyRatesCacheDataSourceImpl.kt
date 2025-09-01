package com.yusufteker.worthy.core.data.service.datasource

import com.yusufteker.worthy.core.domain.model.CachedExchangeRate
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesCacheDataSource
import com.yusufteker.worthy.core.presentation.theme.Constants.ONE_DAY_MILLIS
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

//DataSource = data’ya erişim
class CurrencyRatesCacheDataSourceImpl(
    private val userPrefsManager: UserPrefsManager
) : CurrencyRatesCacheDataSource {

    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    @OptIn(ExperimentalTime::class)
    override suspend fun getRates(base: Currency): CachedExchangeRate? {

        //TRY bazlı tutulup otoamtik deönüştürülecek
        val cached = userPrefsManager.getCachedCurrencyRates() ?: return null
        //{"base":"TRY","rates":{"AUD":0.03725,"CAD":0.03343,"CHF":0.01953,"CNY":0.17341,"EUR":0.02085,"GBP":0.01808,"JPY":3.5809,"NZD":0.04131,"SEK":0.23053,"USD":0.02431},"timestampMillis":1756673319120}

        return try {
            // json formatındaki Ratesleri data classa decode et
            val cachedRates: CachedExchangeRate = json.decodeFromString(cached.json)

            // 24 saat kontrolü Günde 1 api isteği atılabilir
            val now = Clock.System.now().toEpochMilliseconds()
            if (now - cached.timestampMillis >= ONE_DAY_MILLIS) return null
            cachedRates
        } catch (e: Exception) {
            null // NULL DONMESI API ISTEGINI TETIKLER
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun saveRates(cachedRates: CachedExchangeRate) {
        val now = Clock.System.now().toEpochMilliseconds()
        userPrefsManager.setCachedCurrencyRates(
            cachedRatesJson = json.encodeToString(cachedRates), timestampMillis = now
        )
    }
}
