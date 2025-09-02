package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.theme.AppColors.primaryButtonColors
import com.yusufteker.worthy.core.presentation.theme.AppDimens
import com.yusufteker.worthy.core.presentation.theme.AppTypography

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    shape: Shape = RoundedCornerShape(AppDimens.RadiusM),
    textStyle: TextStyle = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    colors: ButtonColors = primaryButtonColors,
    textModifier: Modifier = Modifier // yeni parametre

) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = shape,
        colors = colors,
        modifier = modifier
    ) {
        if (loading) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = colors.disabledContentColor

            )
        } else {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                modifier = textModifier,
                text = text,
                style = textStyle,
                softWrap = true,
                maxLines = 2,
                textAlign = TextAlign.Center
            )

            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}
