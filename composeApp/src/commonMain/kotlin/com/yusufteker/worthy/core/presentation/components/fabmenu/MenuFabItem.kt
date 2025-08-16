package com.yusufteker.worthy.core.presentation.components.fabmenu



import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


/**
 * Tek bir menü öğesini temsil eder.
 *
 * @param icon Buton içinde gösterilecek ikon
 * @param label Butonun yanındaki metin etiketi
 * @param srcIconColor İkonun rengi
 * @param labelTextColor Etiket metninin rengi
 * @param labelBackgroundColor Etiket arka plan rengi
 * @param fabBackgroundColor Butonun arka plan rengi
 */
class MenuFabItem(
    val icon: @Composable () -> Unit,
    val label: String,
    val srcIconColor: Color = Color.White,
    val labelTextColor: Color = Color.White,
    val labelBackgroundColor: Color = Color.Black.copy(alpha = 0.6F),
    val fabBackgroundColor: Color = Color.Unspecified,
)