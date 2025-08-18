package com.yusufteker.worthy.core.data.service

import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.service.CurrencyConverter

class DummyCurrencyConverter : CurrencyConverter {

    private val exchangeRates = mapOf<Pair<Currency, Currency>, Double>(
        Currency.USD to Currency.TRY to 40.0,
        Currency.EUR to Currency.TRY to 50.0,
        Currency.GRAM_GOLD to Currency.TRY to 4000.0,
        Currency.TRY to Currency.TRY to 1.0
    )

    override suspend fun convert(money: Money, to: Currency): Money {
        val rate = exchangeRates[money.currency to to] ?: 1.0
        return Money(money.amount * rate, to)
    }

    override suspend fun convertAll(
        moneyList: List<Money>, to: Currency
    ): List<Money> {
        TODO("Not yet implemented")
    }
}
