package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppDimens
import com.yusufteker.worthy.core.presentation.theme.AppTypography

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 56.dp,
    shape: Shape = RoundedCornerShape(AppDimens.RadiusM),
    textStyle: TextStyle = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = AppColors.primary,
        contentColor   = AppColors.onPrimary,
        disabledContainerColor = AppColors.primary.copy(alpha = 0.3f),
        disabledContentColor   = AppColors.onPrimary.copy(alpha = 0.3f)
    )
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = colors,
        modifier = modifier
            .height(height)
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = text,
            style = textStyle
        )

        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingIcon()
        }
    }
}
