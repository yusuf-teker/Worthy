package com.yusufteker.worthy.core.domain.service

import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money

interface CurrencyConverter {
    suspend fun convert(money: Money, to: Currency): Money
}
