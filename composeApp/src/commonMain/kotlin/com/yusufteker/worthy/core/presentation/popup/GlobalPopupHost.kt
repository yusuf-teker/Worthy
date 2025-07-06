package com.yusufteker.worthy.core.presentation.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.yusufteker.worthy.core.presentation.UiText
import io.github.aakira.napier.Napier
import org.yusufteker.routealarm.core.presentation.popup.LocalPopupManager
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.confirm
import worthy.composeapp.generated.resources.error
import worthy.composeapp.generated.resources.ok

@Composable
fun GlobalPopupHost() {
    val popupManager = LocalPopupManager.current
    val popups = popupManager.popups

    popups.forEach { popup ->
        when (popup) {
            is PopupType.Info -> InfoPopup(
                title = UiText.StringResourceId(popup.title).asString(),
                message = UiText.StringResourceId(popup.message).asString(),
                onDismiss = { popupManager.dismissPopup(popup) }
            )

            is PopupType.Confirm -> ConfirmPopup(
                title = UiText.StringResourceId(popup.title).asString() ,
                message = UiText.StringResourceId(popup.message).asString(),
                onConfirm = {
                    popup.onConfirm()
                    popupManager.dismissPopup(popup)
                },
                onDismiss = { popupManager.dismissPopup(popup) }
            )

            is PopupType.Error -> ErrorPopup(
                message = UiText.StringResourceId(popup.message).asString() ,
                onDismiss = { popupManager.dismissPopup(popup) }
            )

            is PopupType.Custom -> CustomPopup(
                content = popup.content,
                onDismiss = {
                    popupManager.dismissPopup(popup)
                    Napier.d ("CustomPopup global popup onDismiss", tag = "popup")
                }
            )
        }
    }
}



@Composable
private fun InfoPopup(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(UiText.StringResourceId(Res.string.ok).asString())
            }
        }
    )
}

@Composable
private fun ConfirmPopup(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(UiText.StringResourceId(Res.string.cancel).asString())
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text(UiText.StringResourceId(Res.string.confirm).asString())
            }
        }
    )
}

@Composable
private fun ErrorPopup(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(UiText.StringResourceId(Res.string.error).asString()) },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(UiText.StringResourceId(Res.string.ok).asString())
            }
        }
    )
}

@Composable
fun CustomPopup(
    content: @Composable (onDismiss: () -> Unit) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000))
            .clickable(
                onClick = onDismiss,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        content(onDismiss)
    }
}


