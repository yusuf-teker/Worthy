package com.yusufteker.worthy.core.domain

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext


//Safa Call Http istegi atma
suspend inline fun <reified T> safeCall(
    execute : () -> HttpResponse
): Result<T, DataError.Remote> {
    val response: HttpResponse = try {
        execute() // HTP RESPONSE OLUSTUR APIDEN GELEN RAW BILGI
    }catch (e: SocketTimeoutException){
        return Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    }catch (e: UnresolvedAddressException){
        return Result.Error(DataError.Remote.NO_INTERNET)
    }catch (e: Exception){
        coroutineContext.ensureActive() // Exception'in coroutine Cancel olmasi ihtimali var
        return Result.Error(DataError.Remote.UNKNOWN)
    }
    return responseToResult(response)
}




// Gelen response u Result sinifina cevir
suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError.Remote> {

    return when(response.status.value){ // Response un statu koduna göre başarılı veya başarısız
        in 200..299 -> { // 200ler arası başarılı
            try {
                Result.Success(response.body<T>())
            }catch (e: NoTransformationFoundException){
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Remote.SERVER)
        else -> Result.Error(DataError.Remote.UNKNOWN)

    }

}