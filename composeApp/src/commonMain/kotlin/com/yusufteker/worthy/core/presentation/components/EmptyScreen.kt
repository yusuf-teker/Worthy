package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.empty_screen_message

@Composable
fun EmptyScreen(
    icon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = AppColors.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.size(192.dp)
        )
    },
    message: @Composable () -> Unit = {
        Text(
            text = UiText.StringResourceId(Res.string.empty_screen_message).asString(),
            color = AppColors.onBackground.copy(alpha = 0.6f),
            style = AppTypography.titleLarge
        )
    },
    buttonText: String? = null,
    onButtonClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon
        icon()

        Spacer(modifier = Modifier.height(height = 16.dp))

        // Message
        message()

        Spacer(modifier = Modifier.height(24.dp))
        buttonText?.let {
            TextButton(
                onClick = onButtonClick
            ) {
                Text(
                    text = it,
                    color = AppColors.primary,
                    style = AppTypography.labelLarge.copy(
                        fontSize = 24.sp
                    )
                )
            }
        }

    }
}
