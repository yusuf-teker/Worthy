package com.yusufteker.worthy.core.presentation.components.fabmenu

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.formatMoneyText

@Composable
fun MoneyText(
    amount: Double,
    currencySymbol: String,
    style: TextStyle = AppTypography.bodyMedium,
    modifier: Modifier = Modifier
) {
    val locale = Locale.current


    Text(
        modifier= modifier,
        text = "$currencySymbol ${amount.formatMoneyText()}",
        style = style
    )
}
