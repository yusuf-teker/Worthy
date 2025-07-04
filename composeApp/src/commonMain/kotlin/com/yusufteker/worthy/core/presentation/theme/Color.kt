package com.yusufteker.worthy.core.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ColorScheme.customColor: Color
    @Composable get() = if (background == Color(0xFF1C1B1F)) {
        Color(0xFF4CAF50) // Dark tema
    } else {
        Color(0xFF8BC34A) // Light tema
    }
object MyColors {
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

    // Eğer özel renklerin varsa
    val accentGreen: Color @Composable get() = if (MaterialTheme.colorScheme.background == Color(0xFF1C1B1F)) {
        Color(0xFF4CAF50) // Dark
    } else {
        Color(0xFF8BC34A) // Light
    }
}



val primaryLight = Color(0xFF36333E)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFF4D4955)
val onPrimaryContainerLight = Color(0xFFBFB8C7)
val secondaryLight = Color(0xFF605D63)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFE3DEE4)
val onSecondaryContainerLight = Color(0xFF646167)
val tertiaryLight = Color(0xFF3F3035)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFF57464C)
val onTertiaryContainerLight = Color(0xFFCCB5BC)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF93000A)
val backgroundLight = Color(0xFFFDF8F9)
val onBackgroundLight = Color(0xFF1C1B1C)
val surfaceLight = Color(0xFFFDF8F9)
val onSurfaceLight = Color(0xFF1C1B1C)
val surfaceVariantLight = Color(0xFFE6E1E8)
val onSurfaceVariantLight = Color(0xFF48464B)
val outlineLight = Color(0xFF79767C)
val outlineVariantLight = Color(0xFFCAC5CB)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF313031)
val inverseOnSurfaceLight = Color(0xFFF4F0F0)
val inversePrimaryLight = Color(0xFFCAC4D2)
val surfaceDimLight = Color(0xFFDDD9DA)
val surfaceBrightLight = Color(0xFFFDF8F9)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF7F2F3)
val surfaceContainerLight = Color(0xFFF1EDEE)
val surfaceContainerHighLight = Color(0xFFEBE7E8)
val surfaceContainerHighestLight = Color(0xFFE6E1E2)

val primaryLightMediumContrast = Color(0xFF36333E)
val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
val primaryContainerLightMediumContrast = Color(0xFF4D4955)
val onPrimaryContainerLightMediumContrast = Color(0xFFECE4F3)
val secondaryLightMediumContrast = Color(0xFF37353A)
val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
val secondaryContainerLightMediumContrast = Color(0xFF6F6C71)
val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
val tertiaryLightMediumContrast = Color(0xFF3F3035)
val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
val tertiaryContainerLightMediumContrast = Color(0xFF57464C)
val onTertiaryContainerLightMediumContrast = Color(0xFFF9E0E8)
val errorLightMediumContrast = Color(0xFF740006)
val onErrorLightMediumContrast = Color(0xFFFFFFFF)
val errorContainerLightMediumContrast = Color(0xFFCF2C27)
val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
val backgroundLightMediumContrast = Color(0xFFFDF8F9)
val onBackgroundLightMediumContrast = Color(0xFF1C1B1C)
val surfaceLightMediumContrast = Color(0xFFFDF8F9)
val onSurfaceLightMediumContrast = Color(0xFF121112)
val surfaceVariantLightMediumContrast = Color(0xFFE6E1E8)
val onSurfaceVariantLightMediumContrast = Color(0xFF37353A)
val outlineLightMediumContrast = Color(0xFF545157)
val outlineVariantLightMediumContrast = Color(0xFF6F6C72)
val scrimLightMediumContrast = Color(0xFF000000)
val inverseSurfaceLightMediumContrast = Color(0xFF313031)
val inverseOnSurfaceLightMediumContrast = Color(0xFFF4F0F0)
val inversePrimaryLightMediumContrast = Color(0xFFCAC4D2)
val surfaceDimLightMediumContrast = Color(0xFFC9C5C6)
val surfaceBrightLightMediumContrast = Color(0xFFFDF8F9)
val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
val surfaceContainerLowLightMediumContrast = Color(0xFFF7F2F3)
val surfaceContainerLightMediumContrast = Color(0xFFEBE7E8)
val surfaceContainerHighLightMediumContrast = Color(0xFFE0DCDD)
val surfaceContainerHighestLightMediumContrast = Color(0xFFD4D1D1)

val primaryLightHighContrast = Color(0xFF2E2A35)
val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
val primaryContainerLightHighContrast = Color(0xFF4B4753)
val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
val secondaryLightHighContrast = Color(0xFF2D2B30)
val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
val secondaryContainerLightHighContrast = Color(0xFF4B484D)
val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
val tertiaryLightHighContrast = Color(0xFF37282E)
val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
val tertiaryContainerLightHighContrast = Color(0xFF56454B)
val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
val errorLightHighContrast = Color(0xFF600004)
val onErrorLightHighContrast = Color(0xFFFFFFFF)
val errorContainerLightHighContrast = Color(0xFF98000A)
val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
val backgroundLightHighContrast = Color(0xFFFDF8F9)
val onBackgroundLightHighContrast = Color(0xFF1C1B1C)
val surfaceLightHighContrast = Color(0xFFFDF8F9)
val onSurfaceLightHighContrast = Color(0xFF000000)
val surfaceVariantLightHighContrast = Color(0xFFE6E1E8)
val onSurfaceVariantLightHighContrast = Color(0xFF000000)
val outlineLightHighContrast = Color(0xFF2D2B30)
val outlineVariantLightHighContrast = Color(0xFF4B484E)
val scrimLightHighContrast = Color(0xFF000000)
val inverseSurfaceLightHighContrast = Color(0xFF313031)
val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
val inversePrimaryLightHighContrast = Color(0xFFCAC4D2)
val surfaceDimLightHighContrast = Color(0xFFBBB8B9)
val surfaceBrightLightHighContrast = Color(0xFFFDF8F9)
val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
val surfaceContainerLowLightHighContrast = Color(0xFFF4F0F0)
val surfaceContainerLightHighContrast = Color(0xFFE6E1E2)
val surfaceContainerHighLightHighContrast = Color(0xFFD7D3D4)
val surfaceContainerHighestLightHighContrast = Color(0xFFC9C5C6)

val primaryDark = Color(0xFFCAC4D2)
val onPrimaryDark = Color(0xFF322F3A)
val primaryContainerDark = Color(0xFF4D4955)
val onPrimaryContainerDark = Color(0xFFBFB8C7)
val secondaryDark = Color(0xFFCAC5CB)
val onSecondaryDark = Color(0xFF322F34)
val secondaryContainerDark = Color(0xFF4B484D)
val onSecondaryContainerDark = Color(0xFFBBB7BD)
val tertiaryDark = Color(0xFFD8C1C8)
val onTertiaryDark = Color(0xFF3B2C32)
val tertiaryContainerDark = Color(0xFF57464C)
val onTertiaryContainerDark = Color(0xFFCCB5BC)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF141314)
val onBackgroundDark = Color(0xFFE6E1E2)
val surfaceDark = Color(0xFF141314)
val onSurfaceDark = Color(0xFFE6E1E2)
val surfaceVariantDark = Color(0xFF48464B)
val onSurfaceVariantDark = Color(0xFFCAC5CB)
val outlineDark = Color(0xFF938F96)
val outlineVariantDark = Color(0xFF48464B)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE6E1E2)
val inverseOnSurfaceDark = Color(0xFF313031)
val inversePrimaryDark = Color(0xFF615C69)
val surfaceDimDark = Color(0xFF141314)
val surfaceBrightDark = Color(0xFF3A393A)
val surfaceContainerLowestDark = Color(0xFF0F0E0F)
val surfaceContainerLowDark = Color(0xFF1C1B1C)
val surfaceContainerDark = Color(0xFF201F20)
val surfaceContainerHighDark = Color(0xFF2B2A2A)
val surfaceContainerHighestDark = Color(0xFF363435)

val primaryDarkMediumContrast = Color(0xFFE1DAE8)
val onPrimaryDarkMediumContrast = Color(0xFF27242F)
val primaryContainerDarkMediumContrast = Color(0xFF948E9C)
val onPrimaryContainerDarkMediumContrast = Color(0xFF000000)
val secondaryDarkMediumContrast = Color(0xFFE0DBE1)
val onSecondaryDarkMediumContrast = Color(0xFF27252A)
val secondaryContainerDarkMediumContrast = Color(0xFF938F95)
val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
val tertiaryDarkMediumContrast = Color(0xFFEFD6DD)
val onTertiaryDarkMediumContrast = Color(0xFF302227)
val tertiaryContainerDarkMediumContrast = Color(0xFFA08B92)
val onTertiaryContainerDarkMediumContrast = Color(0xFF000000)
val errorDarkMediumContrast = Color(0xFFFFD2CC)
val onErrorDarkMediumContrast = Color(0xFF540003)
val errorContainerDarkMediumContrast = Color(0xFFFF5449)
val onErrorContainerDarkMediumContrast = Color(0xFF000000)
val backgroundDarkMediumContrast = Color(0xFF141314)
val onBackgroundDarkMediumContrast = Color(0xFFE6E1E2)
val surfaceDarkMediumContrast = Color(0xFF141314)
val onSurfaceDarkMediumContrast = Color(0xFFFFFFFF)
val surfaceVariantDarkMediumContrast = Color(0xFF48464B)
val onSurfaceVariantDarkMediumContrast = Color(0xFFE0DBE1)
val outlineDarkMediumContrast = Color(0xFFB5B0B7)
val outlineVariantDarkMediumContrast = Color(0xFF938F95)
val scrimDarkMediumContrast = Color(0xFF000000)
val inverseSurfaceDarkMediumContrast = Color(0xFFE6E1E2)
val inverseOnSurfaceDarkMediumContrast = Color(0xFF2B2A2A)
val inversePrimaryDarkMediumContrast = Color(0xFF4A4652)
val surfaceDimDarkMediumContrast = Color(0xFF141314)
val surfaceBrightDarkMediumContrast = Color(0xFF464445)
val surfaceContainerLowestDarkMediumContrast = Color(0xFF080708)
val surfaceContainerLowDarkMediumContrast = Color(0xFF1E1D1E)
val surfaceContainerDarkMediumContrast = Color(0xFF292728)
val surfaceContainerHighDarkMediumContrast = Color(0xFF333233)
val surfaceContainerHighestDarkMediumContrast = Color(0xFF3F3D3E)

val primaryDarkHighContrast = Color(0xFFF5EDFC)
val onPrimaryDarkHighContrast = Color(0xFF000000)
val primaryContainerDarkHighContrast = Color(0xFFC6C0CE)
val onPrimaryContainerDarkHighContrast = Color(0xFF0C0A14)
val secondaryDarkHighContrast = Color(0xFFF4EEF5)
val onSecondaryDarkHighContrast = Color(0xFF000000)
val secondaryContainerDarkHighContrast = Color(0xFFC6C1C7)
val onSecondaryContainerDarkHighContrast = Color(0xFF0C0B0F)
val tertiaryDarkHighContrast = Color(0xFFFFEBF0)
val onTertiaryDarkHighContrast = Color(0xFF000000)
val tertiaryContainerDarkHighContrast = Color(0xFFD4BDC4)
val onTertiaryContainerDarkHighContrast = Color(0xFF13080D)
val errorDarkHighContrast = Color(0xFFFFECE9)
val onErrorDarkHighContrast = Color(0xFF000000)
val errorContainerDarkHighContrast = Color(0xFFFFAEA4)
val onErrorContainerDarkHighContrast = Color(0xFF220001)
val backgroundDarkHighContrast = Color(0xFF141314)
val onBackgroundDarkHighContrast = Color(0xFFE6E1E2)
val surfaceDarkHighContrast = Color(0xFF141314)
val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
val surfaceVariantDarkHighContrast = Color(0xFF48464B)
val onSurfaceVariantDarkHighContrast = Color(0xFFFFFFFF)
val outlineDarkHighContrast = Color(0xFFF4EEF5)
val outlineVariantDarkHighContrast = Color(0xFFC6C1C8)
val scrimDarkHighContrast = Color(0xFF000000)
val inverseSurfaceDarkHighContrast = Color(0xFFE6E1E2)
val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
val inversePrimaryDarkHighContrast = Color(0xFF4A4652)
val surfaceDimDarkHighContrast = Color(0xFF141314)
val surfaceBrightDarkHighContrast = Color(0xFF514F50)
val surfaceContainerLowestDarkHighContrast = Color(0xFF000000)
val surfaceContainerLowDarkHighContrast = Color(0xFF201F20)
val surfaceContainerDarkHighContrast = Color(0xFF313031)
val surfaceContainerHighDarkHighContrast = Color(0xFF3C3B3C)
val surfaceContainerHighestDarkHighContrast = Color(0xFF484647)


