package com.yusufteker.worthy.screen.subscription.add.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.UiText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.cancel

data class NamedColor(val name: UiText, val color: Color)

@Composable
fun ColorPicker(
    selectedColor: Color?,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
    availableColors: List<NamedColor> = listOf(

        NamedColor(UiText.DynamicString("Netflix"), Color(0xFFE50914)),
        NamedColor(UiText.DynamicString("Spotify"), Color(0xFF1ED760)),
        NamedColor(UiText.DynamicString("YouTube"), Color(0xFFFF0000)),
        NamedColor(UiText.DynamicString("Disney+"), Color(0xFF01147C)),
        NamedColor(UiText.DynamicString("Apple Music"), Color(0xFFFC3C44)),
        NamedColor(UiText.DynamicString("HBO Max"), Color(0xFF28003C)),
        NamedColor(UiText.DynamicString("TikTok"), Color(0xFF010101)),
        NamedColor(UiText.DynamicString("Amazon Prime"), Color(0xFF1399FF)),
        NamedColor(UiText.DynamicString("Hulu"), Color(0xFF1CE783)),
    )
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                brush = if (selectedColor == null) {
                    Brush.sweepGradient(
                        colors = listOf(
                            Color.Red,
                            Color.Yellow,
                            Color.Green,
                            Color.Cyan,
                            Color.Blue,
                            Color.Magenta,
                            Color.Red
                        )
                    )
                } else {
                    Brush.linearGradient(listOf(selectedColor,selectedColor))
                }
            )
            .clickable { showDialog = true }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Color") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    availableColors.chunked(3).forEach { rowColors ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            rowColors.forEach { color ->
                                Column(
                                    modifier = Modifier
                                        .weight(1f), // eşit paylaştırır
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(color.color)
                                            .clickable {
                                                onColorSelected(color.color)
                                                showDialog = false
                                            }
                                    )
                                    Text(
                                        text = color.name.asString(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            // satırda 3'ten az eleman varsa boş yer doldur
                            repeat(3 - rowColors.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(UiText.StringResourceId(Res.string.cancel).asString())
                }
            }
        )
    }
}

fun String.toComposeColor(): Color {

    val hex = this.removePrefix("#")
    return when (hex.length) {
        6 -> {
            val rgb = hex.toLong(16)
            Color(
                red = ((rgb shr 16) and 0xFF) / 255f,
                green = ((rgb shr 8) and 0xFF) / 255f,
                blue = (rgb and 0xFF) / 255f,
                alpha = 1f
            )
        }
        8 -> {
            val argb = hex.toLong(16)
            Color(
                alpha = ((argb shr 24) and 0xFF) / 255f,
                red = ((argb shr 16) and 0xFF) / 255f,
                green = ((argb shr 8) and 0xFF) / 255f,
                blue = (argb and 0xFF) / 255f
            )
        }
        else -> Color.Black
    }
}
fun Color.toHexString(): String {
    val r = (this.red * 255).toInt()
    val g = (this.green * 255).toInt()
    val b = (this.blue * 255).toInt()

    return "#" +
            r.toString(16).padStart(2, '0').uppercase() +
            g.toString(16).padStart(2, '0').uppercase() +
            b.toString(16).padStart(2, '0').uppercase()
}