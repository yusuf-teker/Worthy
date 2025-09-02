package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.theme.AppBrushes
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
    contentPadding: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),

        ) {

        Box(
            modifier = Modifier.fillMaxWidth().background(AppBrushes.menuItemBrush)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.Transparent)
                    .padding(contentPadding), verticalAlignment = Alignment.CenterVertically
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

    }
}

