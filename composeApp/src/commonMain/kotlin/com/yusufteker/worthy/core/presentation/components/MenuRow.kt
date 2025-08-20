package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography

@Composable
fun MenuRow(
    text: String,
    subtitle: String? = null,
    iconPainter: Painter? = null,
    iconVector: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showArrow: Boolean = true,
    backgroundColor: Color = AppColors.surfaceVariant,
    contentPadding: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(
                onClick = onClick,
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        if (iconPainter != null) {
            Icon(
                painter = iconPainter,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
        } else if (iconVector != null) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        // Text & subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(text = text, style = AppTypography.titleMedium)
            if (subtitle != null) {
                Text(text = subtitle, style = AppTypography.bodySmall, color = Color.Gray)
            }
        }

        // Arrow
        if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
