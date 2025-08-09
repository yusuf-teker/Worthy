package com.yusufteker.worthy.core.presentation.util

import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.UIKit.endEditing


@OptIn(ExperimentalForeignApi::class)
actual fun hideKeyboard() {
    Napier.d("hideKeyboard")

    val keyWindow = UIApplication.sharedApplication.keyWindow
    keyWindow?.endEditing(true)

    if (keyWindow == null) {
        UIApplication.sharedApplication.windows.forEach { window ->
            (window as? UIWindow)?.endEditing(true)
        }
    }
}