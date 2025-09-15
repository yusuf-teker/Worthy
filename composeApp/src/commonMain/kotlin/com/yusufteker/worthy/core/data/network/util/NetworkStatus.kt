package com.yusufteker.worthy.core.data.network.util


expect class NetworkStatus {

    fun isOnline(): Boolean

    val statusFlow: kotlinx.coroutines.flow.StateFlow<Boolean>

}