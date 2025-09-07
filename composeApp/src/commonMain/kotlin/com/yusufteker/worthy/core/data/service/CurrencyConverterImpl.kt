package com.yusufteker.worthy.core.data.service

import com.yusufteker.worthy.core.domain.Result
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.repository.CurrencyRatesRepository
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import io.github.aakira.napier.Napier
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CurrencyConverterImpl(private val repository: CurrencyRatesRepository) : CurrencyConverter {
    companion object {
        private const val DEFAULT_RATE = 1.0
    }

    // In-memory cache to avoid multiple repository calls during app runtime.
    // Repository/UserPrefs still enforce daily remote fetch; this cache prevents repeated lookups.
    private val mutex = Mutex()
    private var cachedRates: Map<Currency, Double>? = null

    @OptIn(ExperimentalTime::class)
    private suspend fun ensureRates(base: Currency): Map<Currency, Double> {
        // If we already have in-memory rates, return them immediately.
        cachedRates?.let { return it }

        // Only one coroutine should attempt to fetch and populate cache at a time.
        return mutex.withLock {
            // double-check after acquiring lock
            cachedRates?.let { return it }

            when (val ratesResult = repository.getRates(base)) {
                is Result.Success -> {
                    cachedRates = ratesResult.data
                    Napier.d("CurrencyConverter: Loaded rates into in-memory cache from repository", tag = "CurrencyConverter")
                    ratesResult.data
                }
                is Result.Error -> {
                    Napier.e("CurrencyConverter: Failed to load rates from repository: ${ratesResult.error}", tag = "CurrencyConverter")
                    // fallback to empty map - callers will fall back to DEFAULT_RATE
                    cachedRates = emptyMap()
                    cachedRates!!
                }
            }
        }
    }

    private fun computeRateFromMap(rates: Map<Currency, Double>, from: Currency, to: Currency): Double {
        if (from == to) return DEFAULT_RATE
        val tlToFrom = if (from == Currency.TRY) DEFAULT_RATE else rates[from]
        val tlToTo = if (to == Currency.TRY) DEFAULT_RATE else rates[to]

        if (tlToFrom == null || tlToTo == null) {
            Napier.w("Rate not found for $from or $to, fallback to $DEFAULT_RATE", tag = "CurrencyConverter")
            return DEFAULT_RATE
        }
        return tlToTo / tlToFrom
    }

    private suspend fun convert(amount: Double, from: Currency, to: Currency): Double {
        val rates = ensureRates(Currency.TRY)
        val rate = computeRateFromMap(rates, from, to)
        return amount * rate
    }

    override suspend fun convert(money: Money, to: Currency): Money {
        val convertedAmount = convert(money.amount, money.currency, to)
        return money.copy(amount = convertedAmount, currency = to)
    }

    override suspend fun convertAll(moneyList: List<Money>, to: Currency): List<Money> {
        // Load rates once and reuse for all conversions to avoid repeated repository calls.
        val rates = ensureRates(Currency.TRY)

        // Compute conversion factor for each currency relative to 'to' using the loaded rates.
        return moneyList.map { money ->
            val rate = computeRateFromMap(rates, money.currency, to)
            money.copy(amount = money.amount * rate, currency = to)
        }
    }
}
