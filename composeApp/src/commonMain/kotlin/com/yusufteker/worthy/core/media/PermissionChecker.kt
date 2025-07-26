package com.yusufteker.worthy.core.media

import androidx.compose.runtime.Composable

expect open class PermissionChecker {
    fun hasCameraPermission(): Boolean
    fun requestCameraPermission(onResult: (Boolean) -> Unit)
}

/**
 * Remember platform-specific PermissionChecker instance
 */
@Composable
expect fun rememberPermissionChecker(): PermissionChecker
