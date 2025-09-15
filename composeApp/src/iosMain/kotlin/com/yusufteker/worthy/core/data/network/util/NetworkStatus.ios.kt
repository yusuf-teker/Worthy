package com.yusufteker.worthy.core.data.network.util

import platform.Foundation.NSRunLoop
import platform.Foundation.NSDefaultRunLoopMode
import platform.darwin.dispatch_get_main_queue
import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_monitor_t
import platform.Network.nw_path_status_satisfied
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.dispatch_get_global_queue
import platform.posix.sleep

actual class NetworkStatus {
    private val _statusFlow = MutableStateFlow(true)
    actual val statusFlow: StateFlow<Boolean> get() = _statusFlow

    private val monitor: nw_path_monitor_t = nw_path_monitor_create()

    init {
        // NWPathMonitor queue
        nw_path_monitor_set_queue(
            monitor,
            dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)
        )

        // Update handler -> flow'u güncelle
        nw_path_monitor_set_update_handler(monitor) { path ->
            val isOnline = nw_path_get_status(path) == nw_path_status_satisfied
            _statusFlow.value = isOnline
        }

        nw_path_monitor_start(monitor)
    }

    actual fun isOnline(): Boolean {
        return _statusFlow.value
    }
}