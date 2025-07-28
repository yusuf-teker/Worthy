package com.yusufteker.worthy.core.media

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.yalantis.ucrop.UCrop
import io.github.aakira.napier.Napier
import java.io.File
import java.io.FileOutputStream

import java.io.IOException
@Composable
actual fun rememberImagePicker(): ImagePicker {
    val context = LocalContext.current

    // PickVisualMedia launcher
    val visualMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        val bitmap = uri?.let { loadImageFromUri(context, it) }
        galleryCallback?.invoke(bitmap)
        galleryCallback = null
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        Napier.d(tag = "AndroidImagePicker", message = "cameraLauncher result: $success")
        val bitmap = if (success) {
            currentPhotoUri?.let { loadImageFromUri(context, it) }
        } else null
        cameraCallback?.invoke(bitmap)
        cameraCallback = null
    }

    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Napier.d(tag = "AndroidImagePicker", message = "cropLauncher result: ${result.resultCode}")
        //Crop için yeni ekran açılıyor o ekranda işlem yapılıyor
        // sonrasında bu callback tetikleniyor
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val uri = UCrop.getOutput(result.data!!)
            val bitmap = uri?.let { loadImageFromUri(context, it) }
            //callback imagepicker da cropImage çalıştığında  tanımlanıyor
            cropCallback?.invoke(bitmap)
        } else {
            cropCallback?.invoke(null)
        }
        cropCallback = null
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionCallback?.invoke(isGranted)
        permissionCallback = null
    }

    return remember(visualMediaLauncher, cameraLauncher, permissionLauncher) {
        AndroidImagePicker(
            context = context,
            visualMediaLauncher = visualMediaLauncher,
            cameraLauncher = cameraLauncher,
            permissionLauncher = permissionLauncher,
            cropLauncher = cropLauncher,
        )
    }
}

private fun loadImageFromUri(context: Context, uri: Uri): ImageBitmap? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}


class AndroidImagePicker(
    private val context: Context,
    private val visualMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    private val cameraLauncher: ActivityResultLauncher<Uri>,
    private val permissionLauncher: ActivityResultLauncher<String>,
    private val cropLauncher: ActivityResultLauncher<Intent>,
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

    override fun cropImage(image: ImageBitmap, aspectRatio: Float, onCropped: (ImageBitmap?) -> Unit) {
        cropCallback = onCropped // önce call back setliyoruz.

        // ImageBitmap → Bitmap → File
        val bitmap = image.toAndroidBitmap()
        val inputFile = File(context.cacheDir, "crop_input_${System.currentTimeMillis()}.jpg")
        val outputFile = File(context.cacheDir, "crop_output_${System.currentTimeMillis()}.jpg")

        FileOutputStream(inputFile).use { out ->
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out)
        }

        val inputUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            inputFile
        )
        val outputUri = Uri.fromFile(outputFile)

        val uCrop = UCrop.of(inputUri, outputUri)
            .withAspectRatio(aspectRatio, 1f)
            .withMaxResultSize(1080, 1080)

        // Ucrop başlıyor arka planda uCropActivity açılıyor
        // Sonuc cropLauncher'da yakalanıyor //cropLauncher tanımlandıgı yerde
        cropLauncher.launch(uCrop.getIntent(context))

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