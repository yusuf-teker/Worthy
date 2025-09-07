package com.yusufteker.worthy.core.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.yusufteker.worthy.core.presentation.theme.AppBrushes.screenBackground
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppDimens.ScreenPadding
import com.yusufteker.worthy.core.presentation.util.hideKeyboard

@Composable
fun <T : BaseState> BaseContentWrapper(
    state: T,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = { DefaultLoading() },
    content: @Composable (Modifier) -> Unit
) {

    Box(modifier = modifier.fillMaxSize().background(screenBackground)) {

        val focusManager = LocalFocusManager.current

        Box(
            modifier = modifier.fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        hideKeyboard()
                    })
                }) {
            content(modifier.fillMaxSize())

            if (state.isLoading) {
                Box(
                    modifier = Modifier.matchParentSize().background(Color.Transparent)
                )

                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    loadingContent()
                }
            }
        }
    }
}

@Composable
fun DefaultLoading() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
