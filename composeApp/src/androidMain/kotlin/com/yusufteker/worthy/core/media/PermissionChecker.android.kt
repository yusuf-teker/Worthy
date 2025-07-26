package com.yusufteker.worthy.core.media

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

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
var galleryCallback: ((ImageBitmap?) -> Unit)? = null
var cameraCallback: ((ImageBitmap?) -> Unit)? = null
var permissionCallback: ((Boolean) -> Unit)? = null
var currentPhotoUri: Uri? = null

class AndroidImagePicker(
    private val context: Context,
    private val visualMediaLauncher: androidx.activity.result.ActivityResultLauncher<PickVisualMediaRequest>,
    private val cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>,
    private val permissionLauncher: androidx.activity.result.ActivityResultLauncher<String>
) : ImagePicker {

    override fun pickFromGallery(onResult: (ImageBitmap?) -> Unit) {
        galleryCallback = onResult

        // PickVisualMedia kullan - izin gerekmez
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
            .build()

        visualMediaLauncher.launch(request)
    }

    override fun pickFromCamera(onResult: (ImageBitmap?) -> Unit) {
        cameraCallback = onResult

        if (!isCameraAvailable()) {
            onResult(null)
            return
        }

        // Kamera izni kontrolü
        if (!hasCameraPermission()) {
            requestCameraPermission { granted ->
                if (granted) {
                    launchCamera()
                } else {
                    onResult(null)
                }
            }
        } else {
            launchCamera()
        }
    }

    private fun launchCamera() {
        currentPhotoUri = createImageUri()
        currentPhotoUri?.let { uri ->
            cameraLauncher.launch(uri)
        }
    }

    override fun isCameraAvailable(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    override fun requestCameraPermission(onResult: (Boolean) -> Unit) {
        if (hasCameraPermission()) {
            onResult(true)
        } else {
            permissionCallback = onResult
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    override fun cropImage(image: ImageBitmap, onCropped: (ImageBitmap?) -> Unit) {
        onCropped(image)
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createImageUri(): Uri {
        val imageFile = File(
            context.filesDir,
            "camera_image_${System.currentTimeMillis()}.jpg"
        )

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
    }


}

actual open class PermissionChecker(
    private val context: Context,
    private val permissionLauncher: androidx.activity.result.ActivityResultLauncher<String>
) {

    actual fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
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
    context: Context,
    permissionLauncher: androidx.activity.result.ActivityResultLauncher<String>
) : PermissionChecker(context, permissionLauncher)
