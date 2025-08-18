package com.yusufteker.worthy.core.presentation.popup

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource

sealed class PopupType {
    abstract val onDismiss: () -> Unit

    data class Info(
        val title: StringResource,
        val message: StringResource,
        override val onDismiss: () -> Unit = {}
    ) : PopupType()

    data class Confirm(
        val title: StringResource,
        val message: StringResource,
        val onConfirm: () -> Unit,
        override val onDismiss: () -> Unit = {}
    ) : PopupType()

    data class Error(
        val message: StringResource, override val onDismiss: () -> Unit = {}
    ) : PopupType()

    class Custom(
        val content: @Composable (onDismiss: () -> Unit) -> Unit,
        override val onDismiss: () -> Unit = {}
    ) : PopupType()

}
