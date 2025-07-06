package com.yusufteker.worthy.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.rememberNavController
import com.yusufteker.worthy.app.navigation.AppNavHost
import com.yusufteker.worthy.core.presentation.theme.AppTheme
import com.yusufteker.worthy.core.presentation.theme.AppColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    prefs: DataStore<Preferences>
) {

    val navController = rememberNavController()

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppColors.background
        ) {
            AppNavHost(
                navController = navController
            )

        }
    }
}