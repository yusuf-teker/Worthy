package com.yusufteker.worthy.core.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.yusufteker.worthy.core.presentation.util.hideKeyboard

@Composable
fun <T : BaseState> BaseContentWrapper(
    state: T,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = { DefaultLoading() },
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {

        val focusManager = LocalFocusManager.current

        Box(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        hideKeyboard()
                    })
                }
        ) {
            content()

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Transparent)
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
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
