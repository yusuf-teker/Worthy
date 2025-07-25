package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.media.rememberImagePicker
import com.yusufteker.worthy.core.media.rememberPermissionChecker
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerComponent(
    selectedImage: ImageBitmap?,
    onImageSelected: (ImageBitmap) -> Unit,
    placeholder: String = "Görsel Seçin",
    modifier: Modifier = Modifier,
    imageSize: Dp = 200.dp,
    enabled: Boolean = true,
    cropAspectRatio: Float? = 1f

) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showCameraPermissionDialog by remember { mutableStateOf(false) }

    val imagePicker = rememberImagePicker()

    val permissionChecker = rememberPermissionChecker()



    Box(
        modifier = modifier
            .size(imageSize)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = if (selectedImage != null) Color.Transparent
                else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = enabled) {
                showBottomSheet = true
            },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImage != null) {
            Image(
                bitmap = selectedImage,
                contentDescription = "Seçilen Görsel",
                modifier = Modifier.fillMaxSize().aspectRatio(1f),
                contentScale = ContentScale.Crop,

            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = if (enabled) MaterialTheme.colorScheme.outline
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (enabled) MaterialTheme.colorScheme.outline
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // Kamera İzin Dialog'u
    if (showCameraPermissionDialog) {
        permissionChecker.requestCameraPermission { granted ->
            if (granted) {
                imagePicker.pickFromCamera { bitmap ->

                    bitmap?.let { onImageSelected(it) }
                    showCameraPermissionDialog = false

                }
            }else{
                showCameraPermissionDialog = false
            }
        }
        /*
        AlertDialog(
            onDismissRequest = { showCameraPermissionDialog = false },
            title = { Text("Kamera İzni Gerekli") },
            text = {
                Text("Fotoğraf çekebilmek için kamera iznine ihtiyacımız var.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCameraPermissionDialog = false
                        permissionChecker.requestCameraPermission { granted ->
                            if (granted) {
                                imagePicker.pickFromCamera { bitmap ->
                                    bitmap?.let { onImageSelected(it) }
                                }
                            }
                        }
                    }
                ) {
                    Text("İzin Ver")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCameraPermissionDialog = false }) {
                    Text("İptal")
                }
            }
        )*/
    }

    // Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Görsel Seçin",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Galeri seçeneği - Artık izin gerekmez
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            imagePicker.pickFromGallery { bitmap ->
                                //bitmap?.let { onImageSelected(it) }

                                bitmap?.let {
                                    imagePicker.cropImage(it) { cropped ->
                                        cropped?.let { onImageSelected(it) }
                                    }
                                }

                           }
                           showBottomSheet = false

                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Galeriden Seç",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Kamera seçeneği - İzin kontrolüyle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showBottomSheet = false
                            if (imagePicker.isCameraAvailable()) {
                                if (permissionChecker.hasCameraPermission()) {
                                    imagePicker.pickFromCamera { bitmap ->
                                        bitmap?.let { onImageSelected(it) }

                                    }
                                } else {
                                    showCameraPermissionDialog = true
                                }
                            }
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info, // camera,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (imagePicker.isCameraAvailable())
                            MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (imagePicker.isCameraAvailable()) "Kamera ile Çek"
                        else "Kamera Kullanılamıyor",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (imagePicker.isCameraAvailable())
                            MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Kullanım örneği
@Composable
fun ImagePickerScreen() {
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Modern Görsel Seçici",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ImagePickerComponent(
            selectedImage = selectedImage,
            onImageSelected = { bitmap ->
                selectedImage = bitmap
            },
            placeholder = "Profil fotoğrafı\nekleyin",
            imageSize = 150.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedImage != null) {
            Button(
                onClick = { selectedImage = null }
            ) {
                Text("Görseli Kaldır")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Özellikler:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("• Galeri: İzin gerektirmez (PickVisualMedia)")
                Text("• Kamera: Sadece kamera izni gerekir")
                Text("• Modern Android 13+ desteği")
                Text("• iOS ile tam uyumlu")
            }
        }
    }
}


@Preview
@Composable
fun ImagePickerComponentPreview() {
    MaterialTheme {
        ImagePickerComponent(
            selectedImage = null,
            onImageSelected = {},
            placeholder = "Görsel Seçin",
            imageSize = 200.dp,
            modifier = Modifier.padding(16.dp)
        )
    }
}