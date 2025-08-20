package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yusufteker.worthy.core.presentation.theme.AppColors

@Composable
fun ErrorText(message: String?,
              modifier: Modifier = Modifier) {
    AnimatedVisibility(visible = message != null) {
        Text(
            modifier = modifier,
            text = message.orEmpty(),
            color = AppColors.error
        )
    }
}