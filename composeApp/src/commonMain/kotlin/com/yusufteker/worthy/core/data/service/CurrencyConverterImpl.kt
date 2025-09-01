package com.yusufteker.worthy.core.data.service

import com.yusufteker.worthy.core.domain.Result
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.repository.CurrencyRatesRepository
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import io.github.aakira.napier.Napier

class CurrencyConverterImpl(private val repository: CurrencyRatesRepository) : CurrencyConverter {
    companion object {
        private const val DEFAULT_RATE = 1.0
    }

    private suspend fun getRate(from: Currency, to: Currency): Double {
        if (from == to) return DEFAULT_RATE

        return when (val ratesResult = repository.getRates(Currency.TRY)) {
            is Result.Success -> {
                val tlToFrom = if (from == Currency.TRY) DEFAULT_RATE else ratesResult.data[from]
                val tlToTo = if (to == Currency.TRY) DEFAULT_RATE else ratesResult.data[to]

                if (tlToFrom == null || tlToTo == null) {
                    Napier.w(
                        "Rate not found for $from or $to, fallback to $DEFAULT_RATE",
                        tag = "CurrencyConverter"
                    )
                    DEFAULT_RATE
                } else {
                    tlToTo / tlToFrom
                }
            }

            is Result.Error -> {
                Napier.e("Failed to get rates: ${ratesResult.error}", tag = "CurrencyConverter")
                DEFAULT_RATE
            }
        }
    }

    private suspend fun convert(amount: Double, from: Currency, to: Currency): Double {
        val rate = getRate(from, to)
        return amount * rate
    }

    override suspend fun convert(money: Money, to: Currency): Money {
        val convertedAmount = convert(money.amount, money.currency, to)
        return money.copy(amount = convertedAmount, currency = to)
    }

    override suspend fun convertAll(moneyList: List<Money>, to: Currency): List<Money> {
        return moneyList.map { convert(it, to) }
    }
}
