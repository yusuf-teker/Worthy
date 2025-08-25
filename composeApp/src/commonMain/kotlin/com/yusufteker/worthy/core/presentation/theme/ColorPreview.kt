package com.yusufteker.worthy.core.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview()
@Composable
fun ThemeColorPreview() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
            
        ) {
            Spacer(Modifier.width(100.dp))
            Text(text = "Light", style = AppTypography.labelLarge)
            Text(text = "Dark", style = AppTypography.labelLarge)
        }
        Spacer(modifier = Modifier.height(8.dp))

        val colorNames = listOf(
            "primary" to { scheme: ColorScheme -> scheme.primary },
            "onPrimary" to { scheme: ColorScheme -> scheme.onPrimary },
            "secondary" to { scheme: ColorScheme -> scheme.secondary },
            "onSecondary" to { scheme: ColorScheme -> scheme.onSecondary },
            "background" to { scheme: ColorScheme -> scheme.background },
            "onBackground" to { scheme: ColorScheme -> scheme.onBackground },
            "surface" to { scheme: ColorScheme -> scheme.surface },
            "onSurface" to { scheme: ColorScheme -> scheme.onSurface },
            "error" to { scheme: ColorScheme -> scheme.error },
            "onError" to { scheme: ColorScheme -> scheme.onError }
        )

        colorNames.forEach { (name, colorAccessor) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = name, modifier = Modifier.width(100.dp))
                ColorCircle(color = colorAccessor(lightScheme))
                ColorCircle(color = colorAccessor(darkScheme))
            }
        }
    }
}

@Composable
fun ColorCircle(color: Color) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(color = color, shape = CircleShape)
    )
}