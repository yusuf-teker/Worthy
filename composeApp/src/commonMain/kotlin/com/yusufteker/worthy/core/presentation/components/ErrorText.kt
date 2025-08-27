package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.theme.AppColors
import org.jetbrains.compose.resources.painterResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.ic_error
import worthy.composeapp.generated.resources.ic_info


sealed class UiMessage(val text: String) {
    class Error(text: String) : UiMessage(text)
    class Info(text: String) : UiMessage(text)
}

@Composable
fun MessageText(
    message: UiMessage?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = message != null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (message) {
                is UiMessage.Error -> {
                    Icon(
                        painter = painterResource(Res.drawable.ic_error), // senin error ikonun
                        contentDescription = "Error",
                        tint = AppColors.error,
                        modifier = Modifier.size(16.dp).padding(end = 4.dp)
                    )
                    Text(
                        text = message.text,
                        color = AppColors.error
                    )
                }
                is UiMessage.Info -> {
                    Icon(
                        painter = painterResource(Res.drawable.ic_info), // info ikonun
                        contentDescription = "Info",
                        tint = AppColors.primary,
                        modifier = Modifier.size(16.dp).padding(end = 4.dp)
                    )
                    Text(
                        text = message.text,
                        color = AppColors.primary
                    )
                }
                null -> {}
            }
        }
    }
}
