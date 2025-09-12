package com.yusufteker.worthy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.yusufteker.worthy.app.App
import com.yusufteker.worthy.screen.settings.data.createDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        //setTheme(androidx.appcompat.R.style.Theme_AppCompat_NoActionBar)
        setContent {
            App(
                prefs = remember {
                    createDataStore(this)
                })
        }
    }
}
