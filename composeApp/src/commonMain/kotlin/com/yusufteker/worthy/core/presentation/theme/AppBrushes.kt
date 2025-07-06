package com.yusufteker.worthy.core.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.yusufteker.worthy.core.presentation.theme.AppColors.screenBackgroundColors

object AppBrushes {

    val screenBackground: Brush
        @Composable
        get() = Brush.linearGradient(
            colors = screenBackgroundColors,
            start = Offset.Zero,
            end = Offset.Infinite
        )

}