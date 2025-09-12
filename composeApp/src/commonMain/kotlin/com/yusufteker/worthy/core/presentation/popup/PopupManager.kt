package org.yusufteker.routealarm.core.presentation.popup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.popup.PopupType
import org.jetbrains.compose.resources.StringResource

class PopupManager {
    private val _popups = mutableStateListOf<PopupType>()
    val popups: List<PopupType> get() = _popups

    fun dismissAll() {
        val currentPopups = _popups.toList()
        _popups.clear()
        currentPopups.forEach { popup ->
            try {
                popup.onDismiss()
            } catch (e: Exception) {
                // Handle any dismiss errors
            }
        }
    }

    fun showPopup(popup: PopupType) {
        _popups.add(popup)
    }

    fun dismissPopup(popup: PopupType) {
        popup.onDismiss() // ekstra birşeyler yaplmak istenirse
        _popups.remove(popup)

    }

    fun showInfo(title: StringResource, message: StringResource, onDismiss: () -> Unit = {}) {
        showPopup(PopupType.Info(title, message, onDismiss))
    }

    fun showConfirm(
        title: UiText,
        message: UiText,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit = {},
        confirmLabel: UiText? = null,
        dismissLabel: UiText? = null

    ) {
        showPopup(PopupType.Confirm(title, message, onConfirm, onDismiss))
    }

    fun showError(message: StringResource, onDismiss: () -> Unit = {}) {
        showPopup(PopupType.Error(message, onDismiss))
    }

    fun showCustom(
        content: @Composable (onDismiss: () -> Unit) -> Unit, onDismiss: () -> Unit = {}
    ) {
        showPopup(PopupType.Custom(content, onDismiss))
    }
}
