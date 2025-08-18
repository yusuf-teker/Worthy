package com.yusufteker.worthy.core.media

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
actual fun rememberPermissionChecker(): PermissionChecker {
    val context = LocalContext.current

    // Permission launcher for direct permission checking
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionCallback?.invoke(isGranted)
        permissionCallback = null
    }

    return remember(permissionLauncher) {
        AndroidPermissionChecker(context, permissionLauncher)
    }
}

// Global callbacks for launchers
var galleryCallback: ((PlatformImage?) -> Unit)? = null
var cameraCallback: ((PlatformImage?) -> Unit)? = null
var permissionCallback: ((Boolean) -> Unit)? = null

var cropCallback: ((PlatformImage?) -> Unit)? = null

var currentPhotoUri: Uri? = null


actual open class PermissionChecker(
    private val context: Context,
    private val permissionLauncher: androidx.activity.result.ActivityResultLauncher<String>
) {

    actual fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    actual fun requestCameraPermission(onResult: (Boolean) -> Unit) {
        if (hasCameraPermission()) {
            onResult(true)
        } else {
            permissionCallback = onResult
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}

class AndroidPermissionChecker(
    context: Context, permissionLauncher: androidx.activity.result.ActivityResultLauncher<String>
) : PermissionChecker(context, permissionLauncher)


fun ImageBitmap.toAndroidBitmap(): android.graphics.Bitmap {
    val intArray = IntArray(this.width * this.height)
    this.readPixels(intArray, startX = 0, startY = 0, width = this.width, height = this.height)

    return android.graphics.Bitmap.createBitmap(
        intArray, this.width, this.height, android.graphics.Bitmap.Config.ARGB_8888
    )
}
