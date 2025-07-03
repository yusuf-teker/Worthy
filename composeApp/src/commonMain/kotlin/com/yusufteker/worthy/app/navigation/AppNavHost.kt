package com.yusufteker.worthy.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.yusufteker.worthy.core.presentation.BottomNavigationBar
import com.yusufteker.worthy.core.presentation.theme.MyColors
import com.yusufteker.worthy.feature.onboarding.presentation.OnboardingScreenRoot
import io.github.aakira.napier.Napier
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Routes.OnboardingGraph = Routes.OnboardingGraph
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
            Napier.d("-- Navigated to: $route --", tag = "MoneyWiseNavigation" )
        }
    }


    Scaffold(
        modifier = Modifier.background(MyColors.background),
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
                                popUpTo(Routes.Onboarding) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                    )
                }
/*
                composable<Routes.UserSetupScreen> {
                    UserSetupScreenRoot(
                        contentPadding = innerPadding,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToDashboard = {
                            // Setup tamamlandıktan sonra ana ekrana geçiş
                            navController.navigate(Routes.MainGraph) {
                                popUpTo(Routes.OnboardingGraph) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            // Main Graph (Ana ekran ve yönlendirmeler)
            navigation<Routes.MainGraph>(
                startDestination = Routes.DashboardScreen
            ) {

                composable<Routes.DashboardScreen> {
                    DashboardScreenRoot(
                        contentPadding = innerPadding,
                        onNavigateToEvaluation = {
                            navController.navigate(Routes.ItemEvaluationScreen())
                        },
                        onNavigateToWishlist = {
                            navController.navigate(Routes.WishlistScreen)
                        },
                        onNavigateToTrends = {
                            navController.navigate(Routes.TrendsScreen)
                        },
                        onNavigateToSettings = {
                            navController.navigate(Routes.SettingsScreen)
                        }
                    )
                }

                composable<Routes.ItemEvaluationScreen> { entry ->
                    val args = entry.toRoute<Routes.ItemEvaluationScreen>()
                    val sharedViewModel = entry.sharedKoinViewModel<SharedEvaluationViewModel>(navController = navController)

                    ItemEvaluationScreenRoot(
                        productName = args.productName,
                        price = args.price,
                        category = args.category,
                        contentPadding = innerPadding,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onAddToWishlist = { item ->
                            sharedViewModel.addToWishlist(item)
                            navController.navigate(Routes.WishlistScreen)
                        }
                    )
                }

                composable<Routes.WishlistScreen> { entry ->
                    val sharedViewModel = entry.sharedKoinViewModel<SharedEvaluationViewModel>(navController = navController)
                    val wishlistItems by sharedViewModel.wishlistItems.collectAsStateWithLifecycle()

                    WishlistScreenRoot(
                        contentPadding = innerPadding,
                        wishlistItems = wishlistItems,
                        onNavigateToEvaluation = { productName, price ->
                            navController.navigate(
                                Routes.ItemEvaluationScreen(
                                    productName = productName,
                                    price = price
                                )
                            )
                        },
                        onItemDetailClick = { itemId ->
                            navController.navigate(Routes.WishlistItemDetailScreen(itemId))
                        }
                    )
                }

                composable<Routes.WishlistItemDetailScreen> { backStackEntry ->
                    val args = backStackEntry.toRoute<Routes.WishlistItemDetailScreen>()
                    val itemId = args.itemId

                    WishlistItemDetailScreenRoot(
                        itemId = itemId,
                        contentPadding = innerPadding,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onEvaluateAgain = { productName, price ->
                            navController.navigate(
                                Routes.ItemEvaluationScreen(
                                    productName = productName,
                                    price = price
                                )
                            )
                        }
                    )
                }

                composable<Routes.TrendsScreen> {
                    TrendsScreenRoot(
                        contentPadding = innerPadding,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable<Routes.SettingsScreen> {
                    SettingsScreenRoot(
                        contentPadding = innerPadding,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onResetData = {
                            // Veri sıfırlama sonrası onboarding'e dön
                            navController.navigate(Routes.OnboardingGraph) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }*/
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