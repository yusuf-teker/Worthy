package com.yusufteker.worthy.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.rememberNavController
import com.yusufteker.worthy.app.navigation.AppNavHost
import com.yusufteker.worthy.core.presentation.popup.GlobalPopupHost
import com.yusufteker.worthy.core.presentation.theme.AppTheme
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.util.hideKeyboard
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.yusufteker.routealarm.core.presentation.popup.LocalPopupManager
import org.yusufteker.routealarm.core.presentation.popup.PopupManager

@Composable
@Preview
fun App(
    prefs: DataStore<Preferences> // todo gereklimi kontrol et
) {

    val navController = rememberNavController()
    val popupManager = koinInject<PopupManager>()
    val focusManager = LocalFocusManager.current

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        hideKeyboard()
                    })
                },
            color = AppColors.background
        ) {
            CompositionLocalProvider( // Bu ve altındaki composable'lar popupManager'ı kullanabilir
                LocalPopupManager provides popupManager
            ) {

                AppNavHost(navController = navController)
                GlobalPopupHost() // provider edildiği için popupManager'ı kullanabilir
            }

        }
    }
}