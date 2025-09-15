package com.yusufteker.worthy.core.data.network.util

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class NetworkStatus(private val context: Context) {

    // Flow ile anlık güncellenen bağlantı durumu
    private val _statusFlow: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(isOnline())
    }
   actual val statusFlow: StateFlow<Boolean> get() = _statusFlow

    private val connectivityManager: ConnectivityManager? =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    init {
        // Network değişimlerini dinle
        connectivityManager?.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _statusFlow.value = true
            }

            override fun onLost(network: Network) {
                _statusFlow.value = isOnline()
            }

            override fun onUnavailable() {
                _statusFlow.value = false
            }
        })
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    actual fun isOnline(): Boolean {
        val cm = connectivityManager ?: return false
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
    }
}