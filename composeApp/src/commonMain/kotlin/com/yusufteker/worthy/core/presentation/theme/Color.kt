package com.yusufteker.worthy.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppColors {

    val primary: Color @Composable get() = MaterialTheme.colorScheme.primary
    val onPrimary: Color @Composable get() = MaterialTheme.colorScheme.onPrimary
    val primaryContainer: Color @Composable get() = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer: Color @Composable get() = MaterialTheme.colorScheme.onPrimaryContainer

    val secondary: Color @Composable get() = MaterialTheme.colorScheme.secondary
    val onSecondary: Color @Composable get() = MaterialTheme.colorScheme.onSecondary
    val secondaryContainer: Color @Composable get() = MaterialTheme.colorScheme.secondaryContainer
    val onSecondaryContainer: Color @Composable get() = MaterialTheme.colorScheme.onSecondaryContainer

    val tertiary: Color @Composable get() = MaterialTheme.colorScheme.tertiary
    val onTertiary: Color @Composable get() = MaterialTheme.colorScheme.onTertiary
    val tertiaryContainer: Color @Composable get() = MaterialTheme.colorScheme.tertiaryContainer
    val onTertiaryContainer: Color @Composable get() = MaterialTheme.colorScheme.onTertiaryContainer

    val error: Color @Composable get() = MaterialTheme.colorScheme.error
    val onError: Color @Composable get() = MaterialTheme.colorScheme.onError
    val errorContainer: Color @Composable get() = MaterialTheme.colorScheme.errorContainer
    val onErrorContainer: Color @Composable get() = MaterialTheme.colorScheme.onErrorContainer

    val background: Color @Composable get() = MaterialTheme.colorScheme.background
    val onBackground: Color @Composable get() = MaterialTheme.colorScheme.onBackground
    val surface: Color @Composable get() = MaterialTheme.colorScheme.surface
    val onSurface: Color @Composable get() = MaterialTheme.colorScheme.onSurface

    val surfaceVariant: Color @Composable get() = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceVariant: Color @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

    val outline: Color @Composable get() = MaterialTheme.colorScheme.outline
    val outlineVariant: Color @Composable get() = MaterialTheme.colorScheme.outlineVariant
    val scrim: Color @Composable get() = MaterialTheme.colorScheme.scrim

    val inverseSurface: Color @Composable get() = MaterialTheme.colorScheme.inverseSurface
    val inverseOnSurface: Color @Composable get() = MaterialTheme.colorScheme.inverseOnSurface
    val inversePrimary: Color @Composable get() = MaterialTheme.colorScheme.inversePrimary

    val surfaceDim: Color @Composable get() = MaterialTheme.colorScheme.surfaceDim
    val surfaceBright: Color @Composable get() = MaterialTheme.colorScheme.surfaceBright
    val surfaceContainerLowest: Color @Composable get() = MaterialTheme.colorScheme.surfaceContainerLowest
    val surfaceContainerLow: Color @Composable get() = MaterialTheme.colorScheme.surfaceContainerLow
    val surfaceContainer: Color @Composable get() = MaterialTheme.colorScheme.surfaceContainer
    val surfaceContainerHigh: Color @Composable get() = MaterialTheme.colorScheme.surfaceContainerHigh
    val surfaceContainerHighest: Color @Composable get() = MaterialTheme.colorScheme.surfaceContainerHighest

    // >>>>>>> Custom Color  <<<<<<<<

    val gradientBackgroundColors: List<Color>
        @Composable get() = if (isSystemInDarkTheme()) {
            listOf(
                Color(0xFF0A1116), // Neredeyse siyah-mavi
                Color(0xFF131D29), // Derin gece mavisi
                Color(0xFF1C2A3C),  // Hafif açık gece mavisi
                Color(0xFF131D29),
            )
        } else {
            listOf(
                Color(0xFFF7F9FC), // Çok açık gri-mavi
                Color(0xFFE8F4F8), // Açık mavi-gri
                Color(0xFFD6EAF8)
            )
        }
    val savingsGreen: Color
        @Composable get() = if (isSystemInDarkTheme()) {
            Color(0xFF4CAF50) // Dark - classic green
        } else {
            Color(0xFF8BC34A) // Light - lime green
        }

    val fixedExpenseGray: Color
        @Composable get() = if (isSystemInDarkTheme()) {
            Color(0xFF9E9E9E) // Dark - medium gray
        } else {
            Color(0xFFBDBDBD) // Light - light gray
        }

    val budgetBlue: Color
        @Composable get() = if (isSystemInDarkTheme()) {
            Color(0xFF03A9F4) // Dark - light blue
        } else {
            Color(0xFF81D4FA) // Light - soft sky blue
        }

    // >>>>>>> Color Lists <<<<<<<<
    val screenBackgroundColors: List<Color>
        @Composable get() = listOf(
            background, surface, background
        )

    val icon_red = Color(0xFFFF5722)
    val icon_green = Color(0xFF4CAF50)

    val priorityColors = listOf(
        Color(0xFF4CAF50), // Yeşil
        Color(0xFF8BC34A), // Açık yeşil
        Color(0xFFFFC107), // Sarı
        Color(0xFFFF9800), // Turuncu
        Color(0xFFFF5722)  // Kırmızı
    )

    // >>>>>>> Static Color  <<<<<<<<
    val transparent: Color @Composable get() = Color.Transparent

    val primaryButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            containerColor = AppColors.primary,
            contentColor = AppColors.onPrimary,
            disabledContainerColor = AppColors.primary.copy(alpha = 0.3f),
            disabledContentColor = AppColors.onPrimary.copy(alpha = 0.3f)
        )

    val secondaryButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            containerColor = AppColors.secondary,
            contentColor = AppColors.onSecondary,
            disabledContainerColor = AppColors.secondary.copy(alpha = 0.3f),
            disabledContentColor = AppColors.onSecondary.copy(alpha = 0.3f)
        )

    val txIncomeColor = Color(0xFF4CAF50)
    val txExpenseColor = Color(0xFFF44336)
    val txInstallmentColor = Color(0xFFFF9800)
    val txRefundColor = Color(0xFF2196F3)
}

val primaryLight = Color(0xFF2D638B)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFCDE5FF)
val onPrimaryContainerLight = Color(0xFF084B72)
val secondaryLight = Color(0xFF51606F)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFD4E4F6)
val onSecondaryContainerLight = Color(0xFF394857)
val tertiaryLight = Color(0xFF67587A)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFEDDCFF)
val onTertiaryContainerLight = Color(0xFF4F4061)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF93000A)
val backgroundLight = Color(0xFFF7F9FF)
val onBackgroundLight = Color(0xFF181C20)
val surfaceLight = Color(0xFFF7F9FF)
val onSurfaceLight = Color(0xFF181C20)
val surfaceVariantLight = Color(0xFFDEE3EB)
val onSurfaceVariantLight = Color(0xFF42474E)
val outlineLight = Color(0xFF72787E)
val outlineVariantLight = Color(0xFFC2C7CE)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2D3135)
val inverseOnSurfaceLight = Color(0xFFEEF1F6)
val inversePrimaryLight = Color(0xFF99CCFA)
val surfaceDimLight = Color(0xFFD7DADF)
val surfaceBrightLight = Color(0xFFF7F9FF)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF1F4F9)
val surfaceContainerLight = Color(0xFFEBEEF3)
val surfaceContainerHighLight = Color(0xFFE6E8EE)
val surfaceContainerHighestLight = Color(0xFFE0E2E8)

val primaryLightMediumContrast = Color(0xFF00395A)
val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
val primaryContainerLightMediumContrast = Color(0xFF3D719B)
val onPrimaryContainerLightMediumContrast = Color(0xFFFFFFFF)
val secondaryLightMediumContrast = Color(0xFF293845)
val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
val secondaryContainerLightMediumContrast = Color(0xFF5F6F7E)
val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
val tertiaryLightMediumContrast = Color(0xFF3D3050)
val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
val tertiaryContainerLightMediumContrast = Color(0xFF76668A)
val onTertiaryContainerLightMediumContrast = Color(0xFFFFFFFF)
val errorLightMediumContrast = Color(0xFF740006)
val onErrorLightMediumContrast = Color(0xFFFFFFFF)
val errorContainerLightMediumContrast = Color(0xFFCF2C27)
val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
val backgroundLightMediumContrast = Color(0xFFF7F9FF)
val onBackgroundLightMediumContrast = Color(0xFF181C20)
val surfaceLightMediumContrast = Color(0xFFF7F9FF)
val onSurfaceLightMediumContrast = Color(0xFF0E1215)
val surfaceVariantLightMediumContrast = Color(0xFFDEE3EB)
val onSurfaceVariantLightMediumContrast = Color(0xFF31373D)
val outlineLightMediumContrast = Color(0xFF4D5359)
val outlineVariantLightMediumContrast = Color(0xFF686E74)
val scrimLightMediumContrast = Color(0xFF000000)
val inverseSurfaceLightMediumContrast = Color(0xFF2D3135)
val inverseOnSurfaceLightMediumContrast = Color(0xFFEEF1F6)
val inversePrimaryLightMediumContrast = Color(0xFF99CCFA)
val surfaceDimLightMediumContrast = Color(0xFFC4C7CC)
val surfaceBrightLightMediumContrast = Color(0xFFF7F9FF)
val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
val surfaceContainerLowLightMediumContrast = Color(0xFFF1F4F9)
val surfaceContainerLightMediumContrast = Color(0xFFE6E8EE)
val surfaceContainerHighLightMediumContrast = Color(0xFFDADDE2)
val surfaceContainerHighestLightMediumContrast = Color(0xFFCFD2D7)

val primaryLightHighContrast = Color(0xFF002F4B)
val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
val primaryContainerLightHighContrast = Color(0xFF0E4D75)
val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
val secondaryLightHighContrast = Color(0xFF1F2E3B)
val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
val secondaryContainerLightHighContrast = Color(0xFF3C4B59)
val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
val tertiaryLightHighContrast = Color(0xFF332645)
val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
val tertiaryContainerLightHighContrast = Color(0xFF514364)
val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
val errorLightHighContrast = Color(0xFF600004)
val onErrorLightHighContrast = Color(0xFFFFFFFF)
val errorContainerLightHighContrast = Color(0xFF98000A)
val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
val backgroundLightHighContrast = Color(0xFFF7F9FF)
val onBackgroundLightHighContrast = Color(0xFF181C20)
val surfaceLightHighContrast = Color(0xFFF7F9FF)
val onSurfaceLightHighContrast = Color(0xFF000000)
val surfaceVariantLightHighContrast = Color(0xFFDEE3EB)
val onSurfaceVariantLightHighContrast = Color(0xFF000000)
val outlineLightHighContrast = Color(0xFF272D33)
val outlineVariantLightHighContrast = Color(0xFF444A50)
val scrimLightHighContrast = Color(0xFF000000)
val inverseSurfaceLightHighContrast = Color(0xFF2D3135)
val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
val inversePrimaryLightHighContrast = Color(0xFF99CCFA)
val surfaceDimLightHighContrast = Color(0xFFB6B9BE)
val surfaceBrightLightHighContrast = Color(0xFFF7F9FF)
val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
val surfaceContainerLowLightHighContrast = Color(0xFFEEF1F6)
val surfaceContainerLightHighContrast = Color(0xFFE0E2E8)
val surfaceContainerHighLightHighContrast = Color(0xFFD2D4DA)
val surfaceContainerHighestLightHighContrast = Color(0xFFC4C7CC)

val primaryDark = Color(0xFF99CCFA)
val onPrimaryDark = Color(0xFF003351)
val primaryContainerDark = Color(0xFF084B72)
val onPrimaryContainerDark = Color(0xFFCDE5FF)
val secondaryDark = Color(0xFFB8C8DA)
val onSecondaryDark = Color(0xFF23323F)
val secondaryContainerDark = Color(0xFF394857)
val onSecondaryContainerDark = Color(0xFFD4E4F6)
val tertiaryDark = Color(0xFFD2BFE7)
val onTertiaryDark = Color(0xFF382A4A)
val tertiaryContainerDark = Color(0xFF4F4061)
val onTertiaryContainerDark = Color(0xFFEDDCFF)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF101418)
val onBackgroundDark = Color(0xFFE0E2E8)
val surfaceDark = Color(0xFF101418)
val onSurfaceDark = Color(0xFFE0E2E8)
val surfaceVariantDark = Color(0xFF42474E)
val onSurfaceVariantDark = Color(0xFFC2C7CE)
val outlineDark = Color(0xFF8C9198)
val outlineVariantDark = Color(0xFF42474E)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE0E2E8)
val inverseOnSurfaceDark = Color(0xFF2D3135)
val inversePrimaryDark = Color(0xFF2D638B)
val surfaceDimDark = Color(0xFF101418)
val surfaceBrightDark = Color(0xFF36393E)
val surfaceContainerLowestDark = Color(0xFF0B0F12)
val surfaceContainerLowDark = Color(0xFF181C20)
val surfaceContainerDark = Color(0xFF1C2024)
val surfaceContainerHighDark = Color(0xFF272A2E)
val surfaceContainerHighestDark = Color(0xFF313539)

val primaryDarkMediumContrast = Color(0xFFC1E0FF)
val onPrimaryDarkMediumContrast = Color(0xFF002841)
val primaryContainerDarkMediumContrast = Color(0xFF6395C1)
val onPrimaryContainerDarkMediumContrast = Color(0xFF000000)
val secondaryDarkMediumContrast = Color(0xFFCEDEF0)
val onSecondaryDarkMediumContrast = Color(0xFF182734)
val secondaryContainerDarkMediumContrast = Color(0xFF8392A3)
val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
val tertiaryDarkMediumContrast = Color(0xFFE8D5FD)
val onTertiaryDarkMediumContrast = Color(0xFF2C1F3E)
val tertiaryContainerDarkMediumContrast = Color(0xFF9B8AAF)
val onTertiaryContainerDarkMediumContrast = Color(0xFF000000)
val errorDarkMediumContrast = Color(0xFFFFD2CC)
val onErrorDarkMediumContrast = Color(0xFF540003)
val errorContainerDarkMediumContrast = Color(0xFFFF5449)
val onErrorContainerDarkMediumContrast = Color(0xFF000000)
val backgroundDarkMediumContrast = Color(0xFF101418)
val onBackgroundDarkMediumContrast = Color(0xFFE0E2E8)
val surfaceDarkMediumContrast = Color(0xFF101418)
val onSurfaceDarkMediumContrast = Color(0xFFFFFFFF)
val surfaceVariantDarkMediumContrast = Color(0xFF42474E)
val onSurfaceVariantDarkMediumContrast = Color(0xFFD8DDE4)
val outlineDarkMediumContrast = Color(0xFFADB2BA)
val outlineVariantDarkMediumContrast = Color(0xFF8B9198)
val scrimDarkMediumContrast = Color(0xFF000000)
val inverseSurfaceDarkMediumContrast = Color(0xFFE0E2E8)
val inverseOnSurfaceDarkMediumContrast = Color(0xFF272A2F)
val inversePrimaryDarkMediumContrast = Color(0xFF0B4C73)
val surfaceDimDarkMediumContrast = Color(0xFF101418)
val surfaceBrightDarkMediumContrast = Color(0xFF414549)
val surfaceContainerLowestDarkMediumContrast = Color(0xFF05080B)
val surfaceContainerLowDarkMediumContrast = Color(0xFF1A1E22)
val surfaceContainerDarkMediumContrast = Color(0xFF25282C)
val surfaceContainerHighDarkMediumContrast = Color(0xFF2F3337)
val surfaceContainerHighestDarkMediumContrast = Color(0xFF3A3E42)

val primaryDarkHighContrast = Color(0xFFE6F1FF)
val onPrimaryDarkHighContrast = Color(0xFF000000)
val primaryContainerDarkHighContrast = Color(0xFF95C8F6)
val onPrimaryContainerDarkHighContrast = Color(0xFF000C19)
val secondaryDarkHighContrast = Color(0xFFE6F1FF)
val onSecondaryDarkHighContrast = Color(0xFF000000)
val secondaryContainerDarkHighContrast = Color(0xFFB5C4D6)
val onSecondaryContainerDarkHighContrast = Color(0xFF000C19)
val tertiaryDarkHighContrast = Color(0xFFF7ECFF)
val onTertiaryDarkHighContrast = Color(0xFF000000)
val tertiaryContainerDarkHighContrast = Color(0xFFCEBBE3)
val onTertiaryContainerDarkHighContrast = Color(0xFF110522)
val errorDarkHighContrast = Color(0xFFFFECE9)
val onErrorDarkHighContrast = Color(0xFF000000)
val errorContainerDarkHighContrast = Color(0xFFFFAEA4)
val onErrorContainerDarkHighContrast = Color(0xFF220001)
val backgroundDarkHighContrast = Color(0xFF101418)
val onBackgroundDarkHighContrast = Color(0xFFE0E2E8)
val surfaceDarkHighContrast = Color(0xFF101418)
val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
val surfaceVariantDarkHighContrast = Color(0xFF42474E)
val onSurfaceVariantDarkHighContrast = Color(0xFFFFFFFF)
val outlineDarkHighContrast = Color(0xFFEBF0F8)
val outlineVariantDarkHighContrast = Color(0xFFBEC3CB)
val scrimDarkHighContrast = Color(0xFF000000)
val inverseSurfaceDarkHighContrast = Color(0xFFE0E2E8)
val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
val inversePrimaryDarkHighContrast = Color(0xFF0B4C73)
val surfaceDimDarkHighContrast = Color(0xFF101418)
val surfaceBrightDarkHighContrast = Color(0xFF4D5055)
val surfaceContainerLowestDarkHighContrast = Color(0xFF000000)
val surfaceContainerLowDarkHighContrast = Color(0xFF1C2024)
val surfaceContainerDarkHighContrast = Color(0xFF2D3135)
val surfaceContainerHighDarkHighContrast = Color(0xFF383C40)
val surfaceContainerHighestDarkHighContrast = Color(0xFF43474C)
