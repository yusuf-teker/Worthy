package com.yusufteker.worthy.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun rememberPermissionChecker(): PermissionChecker {
    return remember { IOSPermissionChecker() }
}

actual open class PermissionChecker {

    actual fun hasCameraPermission(): Boolean {
        val authStatus = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
        return authStatus == AVAuthorizationStatusAuthorized
    }

    actual fun requestCameraPermission(onResult: (Boolean) -> Unit) {
        val currentStatus = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)

        when (currentStatus) {
            AVAuthorizationStatusAuthorized -> {
                onResult(true)
            }
            AVAuthorizationStatusNotDetermined -> {
                //requestAccessForMediaType callback'i background thread'de çalışır
                // Bu yüzden, callback'i main thread'e taşımalıyız

                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                    // Callback'i main thread'e taşı )
                    dispatch_async(dispatch_get_main_queue()) {
                        onResult(granted)
                    }
                }
            }
            else -> {
                onResult(false)
            }
        }
    }
}

// Yardımcı sınıf - gerçek tip
class IOSPermissionChecker : PermissionChecker()