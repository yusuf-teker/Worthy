package com.yusufteker.worthy.core.data.service.repository

import com.yusufteker.worthy.core.domain.DataError
import com.yusufteker.worthy.core.domain.Result
import com.yusufteker.worthy.core.domain.model.CachedExchangeRate
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.repository.CurrencyRatesRepository
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesCacheDataSource
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesRemoteDataSource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

//Repository = data kaynaklarını birleştirme
class CurrencyRatesRepositoryImpl(
    private val remote: CurrencyRatesRemoteDataSource,
    private val cache: CurrencyRatesCacheDataSource
) : CurrencyRatesRepository {

    @OptIn(ExperimentalTime::class)
    override suspend fun getRates(base: Currency): Result<Map<Currency, Double>, DataError.Remote> {
        // Cachedeki ratesleri cek not Base Try olarak çekip diğerlerini otomatik hesaplıyoruz
        val cached = cache.getRates(base) // Todo cacheden alma kurallarına internet kontrolüde ekle
        val now = Clock.System.now().toEpochMilliseconds()

        // CACHE DEN ÇEKMEYE UYGUNSA CACHEDEN AL
        if (cached != null) { // null eğerki cache eklenmemiş veya 1 gün geçmiş
            return Result.Success(cached.rates)
        }

        // CACHE'DE YOKSA VEYA 1 GÜN GEÇMEMİŞ İSE APIDEN ÇEK
        val ratesResult = remote.fetchRates(base)

        return when (ratesResult) {
            is Result.Success -> { // APIDEN BAŞARILI ÇEKERSE CACHE KAYDET
                cache.saveRates(
                    CachedExchangeRate(
                        base,
                        ratesResult.data,
                        now // APIDEN ÇEKME TARİHİ
                    )
                )
                Result.Success(ratesResult.data)
            }

            is Result.Error -> Result.Error(ratesResult.error)
        }
    }
}
