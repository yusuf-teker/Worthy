package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppDimens.AppIconSizeSmall
import com.yusufteker.worthy.core.presentation.theme.AppDimens.AppTopBarHeight
import com.yusufteker.worthy.core.presentation.theme.AppTypography

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavIconClick: (() -> Unit)? = null,
    isBack: Boolean = true,
    showDivider: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = modifier.fillMaxWidth()
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
                    modifier = Modifier.align(Alignment.CenterStart).size(AppIconSizeSmall)
                        .clickable { onNavIconClick() })
            }

            // Sağdaki aksiyonlar
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }

        if (showDivider) {
            HorizontalDivider(
                Modifier.padding(top=8.dp), 1.dp, AppColors.outlineVariant
            )
            Spacer(Modifier.height(8.dp))
        }

    }
}
