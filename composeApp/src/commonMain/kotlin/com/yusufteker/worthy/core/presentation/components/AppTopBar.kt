package com.yusufteker.worthy.core.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppDimens.AppIconSizeSmall
import com.yusufteker.worthy.core.presentation.theme.AppDimens.AppTopBarHeight
import com.yusufteker.worthy.core.presentation.theme.AppDimens.Spacing16
import com.yusufteker.worthy.core.presentation.theme.AppTypography

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier.background(AppColors.background),
    title: String,
    onNavIconClick: (() -> Unit)? = null,
    isBack: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTopBarHeight)
    ) {
        // Title her zaman ortada
        Text(
            text = title,
            style = AppTypography.titleLarge,
            color = AppColors.onBackground,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center
        )

        // Sol ikon
        if (onNavIconClick != null) {
            Icon(
                imageVector = if (isBack) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Menu,
                contentDescription = if (isBack) "Back" else "Menu",
                tint = AppColors.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(AppIconSizeSmall)
                    .clickable { onNavIconClick() }
            )
        }

        // Sağdaki aksiyonlar
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
}
