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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.media.PlatformImage
import com.yusufteker.worthy.core.media.rememberImagePicker
import com.yusufteker.worthy.core.media.rememberPermissionChecker
import com.yusufteker.worthy.core.media.toImageBitmap
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import org.jetbrains.compose.resources.painterResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.camera
import worthy.composeapp.generated.resources.gallery
import worthy.composeapp.generated.resources.image_picker_camera
import worthy.composeapp.generated.resources.image_picker_camera_unavailable
import worthy.composeapp.generated.resources.image_picker_gallery
import worthy.composeapp.generated.resources.image_picker_placeholder
import worthy.composeapp.generated.resources.image_picker_selected_image_desc
import worthy.composeapp.generated.resources.image_picker_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerComponent(
    selectedImage: PlatformImage?,
    onImageSelected: (PlatformImage) -> Unit,
    placeholder: UiText = UiText.StringResourceId(Res.string.image_picker_placeholder),
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
                else  AppColors.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = enabled) {
                showBottomSheet = true
            },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImage != null) {
            Image(
                bitmap = selectedImage.toImageBitmap(),
                contentDescription = UiText.StringResourceId(Res.string.image_picker_selected_image_desc).asString(),
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
                    tint = if (enabled) AppColors.outline
                    else  AppColors.outline.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = placeholder.asString(),
                    style = AppTypography.bodyMedium,
                    color = if (enabled)  AppColors.outline
                    else  AppColors.outline.copy(alpha = 0.5f),
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

                    bitmap?.let {
                        imagePicker.cropImage(it) { cropped ->
                            cropped?.let { onImageSelected(it) }
                        }
                    }
                    showCameraPermissionDialog = false

                }
            }else{
                showCameraPermissionDialog = false
            }
        }
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
                    text = UiText.StringResourceId(Res.string.image_picker_title).asString(),
                    style = AppTypography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Galeri seçeneği - Artık izin gerekmez
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            imagePicker.pickFromGallery { bitmap ->
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
                        painter = painterResource(resource = Res.drawable.gallery),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = UiText.StringResourceId(Res.string.image_picker_gallery).asString(),
                        style = AppTypography.bodyLarge
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
                                        bitmap?.let {
                                            imagePicker.cropImage(it) { cropped ->
                                                cropped?.let { onImageSelected(it) }
                                            }
                                        }

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
                        painter = painterResource(resource = Res.drawable.camera),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (imagePicker.isCameraAvailable())
                            AppColors.onSurface
                        else AppColors.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (imagePicker.isCameraAvailable())
                            UiText.StringResourceId(Res.string.image_picker_camera).asString()
                        else UiText.StringResourceId(Res.string.image_picker_camera_unavailable).asString(),
                        style = AppTypography.bodyLarge,
                        color = if (imagePicker.isCameraAvailable())
                            AppColors.onSurface
                        else AppColors.onSurface.copy(alpha = 0.5f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
