package com.yusufteker.worthy.core.presentation.components.fabmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember



/**
 * Menü FAB'ın durumunu tutan state sınıfı.
 * Collapsed → Kapalı
 * Expanded → Açık
 */
@Stable
class MenuFabState {
    val menuFabStateEnum: MutableState<MenuFabStateEnum> =
        mutableStateOf(MenuFabStateEnum.Collapsed)
}

@Composable
fun rememberMenuFabState() = remember { MenuFabState() }

enum class MenuFabStateEnum {
    Collapsed,
    Expanded
}