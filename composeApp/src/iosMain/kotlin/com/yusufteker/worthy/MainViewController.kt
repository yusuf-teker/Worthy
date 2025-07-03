package com.yusufteker.worthy

import androidx.compose.ui.window.ComposeUIViewController
import com.yusufteker.worthy.app.App
import com.yusufteker.worthy.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun MainViewController() = ComposeUIViewController(
    configure = {

        Napier.base(DebugAntilog()) // init logger

        //Koin
        initKoin()

    }
) {
    App()
}