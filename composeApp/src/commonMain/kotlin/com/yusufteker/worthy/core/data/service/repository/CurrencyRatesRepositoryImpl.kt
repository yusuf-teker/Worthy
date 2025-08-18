package com.yusufteker.worthy.core.data.service.repository

import com.yusufteker.worthy.core.data.service.model.CachedRates
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.repository.CurrencyRatesRepository
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesCacheDataSource
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesRemoteDataSource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CurrencyRatesRepositoryImpl(
    private val cache: CurrencyRatesCacheDataSource,
    private val remote: CurrencyRatesRemoteDataSource
) : CurrencyRatesRepository {

    @OptIn(ExperimentalTime::class)
    override suspend fun getRates(base: Currency): Map<Currency, Double> {
        return try {
            // Simüle edilen online veri (gerçekte burası API çağrısı olacak)
            val rates = when (base) {
                Currency.USD -> mapOf(
                    Currency.TRY to 40.0,
                    Currency.EUR to 0.90,
                    Currency.USD to 1.0,
                    Currency.GRAM_GOLD to 0.013,
                    Currency.GBP to 0.77
                )

                Currency.TRY -> mapOf(
                    Currency.USD to 0.031,
                    Currency.EUR to 0.028,
                    Currency.TRY to 1.0,
                    Currency.GRAM_GOLD to 0.00041
                )

                Currency.EUR -> mapOf(
                    Currency.USD to 1.1,
                    Currency.TRY to 35.0,
                    Currency.EUR to 1.0,
                    Currency.GRAM_GOLD to 0.014
                )

                Currency.GBP -> mapOf(
                    Currency.USD to 1.3,
                    Currency.TRY to 40.0,
                    Currency.EUR to 1.2,
                    Currency.GRAM_GOLD to 0.015
                )

                Currency.GRAM_GOLD -> mapOf(
                    Currency.USD to 77.0,
                    Currency.TRY to 250000.0,
                    Currency.EUR to 70.0,
                    Currency.GBP to 65.0
                )

                Currency.BTC -> mapOf(
                    Currency.USD to 50000.0,
                    Currency.TRY to 2000000.0,
                    Currency.EUR to 45000.0,
                    Currency.GBP to 40000.0
                )

            }

            // Yeni veriyi cache’e yaz
            cache.saveRates(CachedRates(base, rates, Clock.System.now().toEpochMilliseconds()))
            rates
        } catch (_: Exception) {
            // Online başarısızsa cache’den oku
            val cached = cache.getRates(base)
            cached?.rates ?: emptyMap()
        }
    }
}