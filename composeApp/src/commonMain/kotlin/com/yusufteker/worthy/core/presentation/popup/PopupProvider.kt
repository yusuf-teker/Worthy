package org.yusufteker.routealarm.core.presentation.popup

import androidx.compose.runtime.compositionLocalOf

val LocalPopupManager = compositionLocalOf<PopupManager> {
    error("No PopupManager provided")
}