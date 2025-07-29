package com.yusufteker.worthy.core.data.service.datasource

import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesRemoteDataSource

class CurrencyRatesRemoteDataSourceImpl : CurrencyRatesRemoteDataSource {
    override suspend fun fetchRates(base: Currency): Map<Currency, Double> {
        // Dummy veri (gerçek API çağrısı gelecekte buraya yazılır)
        return when (base) {
            Currency.USD -> mapOf(
                Currency.USD to 1.0,
                Currency.TRY to 32.0,
                Currency.EUR to 0.91,
                Currency.GRAM_GOLD to 0.013
            )
            // diğer para birimleri için de benzer map'ler
            else -> emptyMap()
        }
    }
}