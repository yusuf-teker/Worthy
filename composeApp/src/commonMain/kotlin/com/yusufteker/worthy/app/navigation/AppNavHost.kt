package com.yusufteker.worthy.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.yusufteker.worthy.core.presentation.BottomNavigationBar
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppDimens.ScreenPadding
import com.yusufteker.worthy.screen.dashboard.presentation.DashboardScreenRoot
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.screen.onboarding.presentation.OnboardingScreenRoot
import com.yusufteker.worthy.screen.settings.presentation.SettingsScreenRoot
import com.yusufteker.worthy.screen.wallet.presentation.WalletScreenRoot
import com.yusufteker.worthy.screen.wishlist.add.presentation.WishlistAddScreenRoot
import com.yusufteker.worthy.screen.wishlist.detail.presentation.WishlistDetailScreenRoot
import com.yusufteker.worthy.screen.wishlist.list.presentation.WishlistScreenRoot
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    onboardingManager: OnboardingManager = koinInject()
){
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route?.substringAfterLast(".")
    val systemBars = WindowInsets.systemBars
    val bottomPadding = with(LocalDensity.current) { systemBars.getBottom(this).toDp() }

    // Bottom bar'ın gösterilmesi gereken ekranlar
    val showBottomBar = currentRoute in listOf(
        Routes.Dashboard .toString(),
        Routes.Wishlist.toString(),
        Routes.Trends .toString(),
        Routes.Settings.toString(),
    )

    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            Napier.d("-- Navigated to: $route --", tag = "AppNavHost" )
        }
    }

    val onboardingDone by produceState<Boolean?>(initialValue = null) {
        value = onboardingManager.isOnboardingCompleted()
    }

    if (onboardingDone == null) {
        Box(
            Modifier.fillMaxSize().background(AppColors.background),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        return
    }

    val startDestination =
        if (onboardingDone == true) Routes.MainGraph else Routes.OnboardingGraph



    Scaffold(
        modifier = Modifier.background(AppColors.background).padding(horizontal = ScreenPadding)
        ,

        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    modifier = Modifier.padding(bottom = bottomPadding),
                    currentRoute = currentRoute ?: "",
                    onItemSelected = { route ->
                        if (route.toString() != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(Routes.Dashboard.toString()) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = startDestination) {

            // Onboarding Graph
            navigation<Routes.OnboardingGraph>(
                startDestination = Routes.Onboarding
            ) {

                composable<Routes.Onboarding> {

                    OnboardingScreenRoot(
                        onGetStarted = {
                            navController.navigate(Routes.MainGraph) {
                                popUpTo(Routes.OnboardingGraph) { inclusive = true }
                                launchSingleTop = true
                            }

                        },
                    )
                }
            }

            // Main Graph (Ana ekran ve yönlendirmeler)
            navigation<Routes.MainGraph>(
                startDestination = Routes.Dashboard
            ) {

                composable<Routes.Dashboard> {

                    DashboardScreenRoot(
                        contentPadding = innerPadding,
                        onNavigateToEvaluation = {
                            //navController.navigate(Routes.ItemEvaluationScreen())
                        },
                        onNavigateToWishlist = {
                            //navController.navigate(Routes.WishlistScreen)
                        },/*
                        onNavigateToTrends = {
                            //navController.navigate(Routes.TrendsScreen)
                        },
                        onNavigateToSettings = {
                            //navController.navigate(Routes.SettingsScreen)
                        }*/
                    )
                }


                navigation<Routes.WishlistGraph>(
                    startDestination = Routes.Wishlist
                ){
                    composable<Routes.Wishlist> { entry ->

                        WishlistScreenRoot(
                            contentPadding = innerPadding,
                            navigateToWishlistAdd = {
                                navController.navigate(Routes.WishlistAdd)
                            },
                            navigateToWishlistDetail = { wishlistId ->
                                navController.navigate(Routes.WishlistDetail(wishlistId))
                            }
                        )


                    }
                    composable<Routes.WishlistAdd> {
                        WishlistAddScreenRoot(
                            contentPadding = innerPadding,
                        )
                    }

                    composable<Routes.WishlistDetail> {
                        val args = it.toRoute<Routes.WishlistDetail>()
                        val wishlistId = args.id
                        WishlistDetailScreenRoot(
                            contentPadding = innerPadding,
                        )
                    }
                }
                composable<Routes.Wallet> {
                    WalletScreenRoot(
                        contentPadding = innerPadding,
                    )
                }


                composable<Routes.Settings> {
                    SettingsScreenRoot(
                        contentPadding = innerPadding,
                    )
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = this.destination.parent?.route ?: return koinViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}