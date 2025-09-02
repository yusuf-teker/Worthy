package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
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

@Composable
fun BorderTransparentButton(
    onClick: () -> Unit,
    text: String,
    leadingIcon: (@Composable (() -> Unit))? = null,

    trailingIcon: (@Composable (() -> Unit))? = null,
    borderColor: Color = Color.Gray,
    textColor: Color = Color.Gray,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 0.dp
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, borderColor),

        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = verticalPadding,
            bottom = verticalPadding
        )    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {

            if (leadingIcon != null){
                leadingIcon()
            }
            Text(
                text = text,
                color = textColor,
                modifier = Modifier.padding(end = if (trailingIcon != null) 8.dp else 0.dp)
            )
            if (trailingIcon != null) {
                trailingIcon()
            }

        }
    }
}
