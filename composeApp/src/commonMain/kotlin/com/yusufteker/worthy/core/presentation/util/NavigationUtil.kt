package com.yusufteker.worthy.core.presentation.util

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.yusufteker.worthy.app.navigation.Routes

fun isOnCurrentDestination(destinationRoute: String, currentDestination: NavDestination?): Boolean {
    return  currentDestination
        ?.hierarchy
        ?.any {
            it.route?.contains(destinationRoute) ?: false
        } == true

}

fun NavHostController.navigateTo(route: Routes){
    if (route == Routes.Back) this.popBackStack()
    else this.navigate(route)
}