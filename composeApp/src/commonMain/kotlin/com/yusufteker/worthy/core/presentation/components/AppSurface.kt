package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Surface composable extension for gradient backgrounds
@Composable
fun AppSurface(
    color: Brush,
    shape: androidx.compose.ui.graphics.Shape,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.background(color, shape),
        shape = shape,
        color = Color.Transparent,
        content = content
    )
}