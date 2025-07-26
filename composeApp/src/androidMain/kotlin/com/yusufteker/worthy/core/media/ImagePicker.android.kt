package com.yusufteker.worthy.core.media

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

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
        val bitmap = if (success) {
            currentPhotoUri?.let { loadImageFromUri(context, it) }
        } else null
        cameraCallback?.invoke(bitmap)
        cameraCallback = null
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
            permissionLauncher = permissionLauncher
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