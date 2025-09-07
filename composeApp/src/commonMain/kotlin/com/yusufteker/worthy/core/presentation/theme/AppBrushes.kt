package com.yusufteker.worthy.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppBrushes {

    val screenBackground: Brush
        @Composable get() = Brush.linearGradient(
            colors = AppColors.gradientBackgroundColors, start = Offset.Zero, end = Offset.Infinite
        )
    val screenBackgroundReversed: Brush
        @Composable get() = Brush.linearGradient(
            colors = AppColors.gradientBackgroundColors,
            start = Offset.Infinite,
            end = Offset.Zero
        )


    val cardItemBrush: Brush
        @Composable get() = if (isSystemInDarkTheme()) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF161E27), // Arka plan renginden biraz açık
                    Color(0xFF1A242F), // Orta ton
                    Color(0xFF1D2935), // Biraz daha açık
                ), start = Offset(0f, 0f), end = Offset.Infinite
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFFF8F9FA), // Açık gri-beyaz
                    Color(0xFFE9ECEF), // Hafif gri
                    Color(0xFFF1F3F4), // Çok açık gri
                ), start = Offset(0f, 0f), end = Offset.Infinite
            )
        }

    val menuItemBrush: Brush
        @Composable get() = if (isSystemInDarkTheme()) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1E2732), // Biraz daha açık koyu mavi-siyah
                    Color(0xFF232F3C), // Orta ton
                    Color(0xFF2A3846)  // Daha açık ton
                ), start = Offset(0f, 0f), end = Offset.Infinite
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFFF8F9FA), // Açık gri-beyaz
                    Color(0xFFE9ECEF), // Hafif gri
                    Color(0xFFF1F3F4)  // Çok açık gri
                ), start = Offset(0f, 0f), end = Offset.Infinite
            )
        }

    val cardItemBrushWithBorder: Brush
        @Composable get() = if (isSystemInDarkTheme()) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1A242F), // Solid renk
                    Color(0xFF1C2A3C), // Arka plan renginden alıntı
                    Color(0xFF1E2C40), // Biraz açık
                ), start = Offset(0f, 0f), end = Offset.Infinite
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFFF5F5F5),
                    Color(0xFFEEEEEE),
                    Color(0xFFF0F0F0),
                ), start = Offset(0f, 0f), end = Offset.Infinite
            )
        }
    val cardBorderColor: Color
        @Composable get() = if (isSystemInDarkTheme()) {
            Color(0xFF2A3A4C) // Solid gri-mavi
        } else {
            Color(0xFFE0E0E0) // Solid açık gri
        }

}