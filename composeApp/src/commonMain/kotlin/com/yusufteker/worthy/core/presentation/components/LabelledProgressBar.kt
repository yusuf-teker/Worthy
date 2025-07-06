package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTheme
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LabelledProgressBar(
    label: String,
    progress: Float,          // 0f–1f
    trailingText: String,     // “600”, “750” gibi
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = AppTypography.bodyLarge,
                color = AppColors.onBackground
            )
           Text(
                text = trailingText,
                style = AppTypography.bodyLarge,
                color = AppColors.onBackground
            )
        }
        Spacer(Modifier.height(8.dp))

        // Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                .background(AppColors.primary.copy(alpha = .25f))
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(AppColors.primary)
            )
        }
    }
}

@Preview
@Composable
fun LabelledProgressBarPreview() {
    AppTheme(
        darkTheme = true
    ) {
        Column(Modifier.background(AppColors.background)) {
            LabelledProgressBar(
                label = "Label",
                progress = 0.75f,
                trailingText = "600"
            )
        }
    }
}
