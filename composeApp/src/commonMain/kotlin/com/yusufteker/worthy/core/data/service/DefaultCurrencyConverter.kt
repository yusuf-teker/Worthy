package com.yusufteker.worthy.core.data.service

import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.repository.CurrencyRatesRepository
import com.yusufteker.worthy.core.domain.service.CurrencyConverter

class DefaultCurrencyConverter(
    private val repository: CurrencyRatesRepository
) : CurrencyConverter {
    override suspend fun convert(money: Money, to: Currency): Money {
        val rate = repository.getRates(money.currency)[to] ?: return money
        return Money(money.amount * rate, to)
    }
}

