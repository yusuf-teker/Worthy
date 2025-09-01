package com.yusufteker.worthy.core.data.service.datasource

import com.yusufteker.worthy.core.data.service.model.ExchangeRateResponse
import com.yusufteker.worthy.core.domain.DataError
import com.yusufteker.worthy.core.domain.Result
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesRemoteDataSource
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

//DataSource = data’ya erişim
class CurrencyRatesRemoteDataSourceImpl(
    private val client: HttpClient
) : CurrencyRatesRemoteDataSource {

    override suspend fun fetchRates(base: Currency): Result<Map<Currency, Double>, DataError.Remote> {
        Napier.d("fetchRates started. Base: ${base.name}", tag = "CurrencyRatesRemote")

        val response = try {
            client.get("https://api.frankfurter.dev/v1/latest") {
                parameter("base", base.name)
            }
        } catch (e: UnresolvedAddressException) {
            Napier.e("No internet: ${e.message}", e, tag = "CurrencyRatesRemote")
            return Result.Error(DataError.Remote.NO_INTERNET)
        } catch (e: SerializationException) {
            Napier.e("Serialization error: ${e.message}", e, tag = "CurrencyRatesRemote")
            return Result.Error(DataError.Remote.SERIALIZATION)
        } catch (e: Exception) {
            Napier.e("Unknown error: ${e.message}", e, tag = "CurrencyRatesRemote")
            return Result.Error(DataError.Remote.UNKNOWN)
        }

        Napier.d("Raw response status: ${response.status.value}", tag = "CurrencyRatesRemote")

        return when (response.status.value) {
            in 200..299 -> {
                try {
                    val dto: ExchangeRateResponse = response.body()
                    Napier.d("RAW JSON parsed: $dto", tag = "CurrencyRatesRemote")

                    val mappedRates = dto.rates.mapNotNull { (code, value) ->
                        try {
                            Currency.valueOf(code) to value
                        } catch (e: IllegalArgumentException) {
                            Napier.w(
                                "Unsupported currency skipped: $code",
                                tag = "CurrencyRatesRemote"
                            )
                            null
                        }
                    }.toMap()
                    Napier.d("Mapped rates: $mappedRates", tag = "CurrencyRatesRemote")

                    Result.Success(mappedRates)
                } catch (e: Exception) {
                    Napier.e("Parsing error: ${e.message}", e, tag = "CurrencyRatesRemote")
                    Result.Error(DataError.Remote.SERIALIZATION)
                }
            }

            408 -> {
                Napier.e("Request timeout", tag = "CurrencyRatesRemote")
                Result.Error(DataError.Remote.REQUEST_TIMEOUT)
            }

            else -> {
                Napier.e(
                    "Unexpected response: ${response.status.value}",
                    tag = "CurrencyRatesRemote"
                )
                Result.Error(DataError.Remote.UNKNOWN)
            }
        }
    }
}

