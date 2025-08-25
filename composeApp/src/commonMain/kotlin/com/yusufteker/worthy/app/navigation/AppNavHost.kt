package com.yusufteker.worthy.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppDimens.ScreenPadding
import com.yusufteker.worthy.core.presentation.util.isOnCurrentDestination
import com.yusufteker.worthy.core.presentation.util.navigateTo
import com.yusufteker.worthy.screen.addtransaction.presentation.AddTransactionScreenRoot
import com.yusufteker.worthy.screen.analytics.presentation.AnalyticsScreenRoot
import com.yusufteker.worthy.screen.card.add.presentation.AddCardScreenRoot
import com.yusufteker.worthy.screen.card.list.presentation.CardListScreenRoot
import com.yusufteker.worthy.screen.dashboard.presentation.DashboardScreenRoot
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.screen.onboarding.presentation.OnboardingScreenRoot
import com.yusufteker.worthy.screen.settings.presentation.SettingsScreenRoot
import com.yusufteker.worthy.screen.subscription.add.presentation.AddSubscriptionScreenRoot
import com.yusufteker.worthy.screen.subscription.list.presentation.SubscriptionListScreenRoot
import com.yusufteker.worthy.screen.transaction.add.presentation.components.AddFabMenu
import com.yusufteker.worthy.screen.wallet.presentation.WalletScreenRoot
import com.yusufteker.worthy.screen.wishlist.add.presentation.WishlistAddScreenRoot
import com.yusufteker.worthy.screen.wishlist.detail.presentation.WishlistDetailScreenRoot
import com.yusufteker.worthy.screen.wishlist.list.presentation.WishlistScreenRoot
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.card
import worthy.composeapp.generated.resources.expenses
import worthy.composeapp.generated.resources.income

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController, onboardingManager: OnboardingManager = koinInject()
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route?.substringAfterLast(".")
    val systemBars = WindowInsets.systemBars
    val bottomPadding = with(LocalDensity.current) { systemBars.getBottom(this).toDp() }

    // Bottom bar'ın gösterilmesi gereken ekranlar
    val showBottomBar = currentRoute in listOf(
        Routes.Dashboard.toString(),
        Routes.Wishlist.toString(),
        Routes.Analytics.toString(),
        Routes.Settings.toString(),
        Routes.AddTransaction.toString(),
    )

    var showAddFabMenu by remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            Napier.d("-- Navigated to: $route --", tag = "AppNavHost")
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

    val startDestination = if (onboardingDone == true) Routes.MainGraph else Routes.OnboardingGraph

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination


    Scaffold(
        modifier = Modifier.background(AppColors.background).padding(horizontal = ScreenPadding),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    modifier = Modifier.padding(bottom = bottomPadding),
                    currentRoute = currentRoute ?: "",
                    onItemSelected = { route ->

                        if (isOnCurrentDestination(route.toString(),currentDestination)){
                            return@BottomNavigationBar
                        }
                        if (route.isAddTransaction()) {
                            showAddFabMenu = !showAddFabMenu
                        } else if (route.toString() != currentRoute) {
                            showAddFabMenu = false
                            navController.navigate(route) {
                                popUpTo(Routes.Dashboard.toString()) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    })
            }
        }) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {

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
                            contentPadding = innerPadding, onNavigateTo = { route ->
                                navController.navigateTo(route)
                            })
                    }

                    navigation<Routes.AnalyticsGraph>(
                        startDestination = Routes.Analytics
                    ) {

                        composable<Routes.Analytics> {
                            AnalyticsScreenRoot(
                                contentPadding = innerPadding,
                                onNavigateTo = { route ->
                                    navController.navigateTo(route)
                                }
                            )
                        }
                    }

                    navigation<Routes.WishlistGraph>(
                        startDestination = Routes.Wishlist
                    ) {
                        composable<Routes.Wishlist> { entry ->

                            WishlistScreenRoot(
                                contentPadding = innerPadding,
                                onNavigateTo = { route ->
                                    navController.navigateTo(route)
                                }
                            )


                        }
                        composable<Routes.WishlistAdd> {
                            WishlistAddScreenRoot(
                                contentPadding = innerPadding,
                                onNavigateTo = { route ->
                                    navController.navigateTo(route)
                                }
                            )
                        }

                        composable<Routes.WishlistDetail> { // Todo Wishlist Detay ekranı eklenecek
                            val args = it.toRoute<Routes.WishlistDetail>()
                            val wishlistId = args.id
                            WishlistDetailScreenRoot(
                                contentPadding = innerPadding,
                                wishlistId = wishlistId,
                                onNavigateTo = { route ->
                                    navController.navigateTo(route)
                                }
                            )
                        }
                    }

                    navigation<Routes.CardGraph>(
                        startDestination = Routes.CardList
                    ) {

                        composable<Routes.CardList> {
                            CardListScreenRoot(
                                contentPadding = innerPadding,
                                onNavigateTo = { route ->
                                    navController.navigateTo(route)
                                }
                            )
                        }
                    }

                    composable<Routes.AddTransaction> {
                        val args = it.toRoute<Routes.AddTransaction>()
                        val isIncomeByDefault = args.isIncome
                        AddTransactionScreenRoot(
                            isIncomeByDefault = isIncomeByDefault,
                            contentPadding = innerPadding,
                            onNavigateTo = { route ->
                                navController.navigateTo(route)
                            })
                    }

                    composable<Routes.AddCard> {
                        AddCardScreenRoot(
                            contentPadding = innerPadding, onNavigateTo = { route ->
                                navController.navigateTo(route)
                            }
                        )
                    }

                    composable<Routes.Wallet> {
                        WalletScreenRoot(
                            contentPadding = innerPadding,
                        )
                    }


                    composable<Routes.Settings> {
                        SettingsScreenRoot(
                            contentPadding = innerPadding,
                            onNavigateTo = { route ->
                                navController.navigateTo(route)
                            }
                        )
                    }

                    navigation<Routes.SubscriptionGraph>(
                        startDestination = Routes.SubscriptionList
                    ) {

                        composable<Routes.SubscriptionList> {
                            SubscriptionListScreenRoot(
                                contentPadding = innerPadding,
                                onNavigateTo = { route ->
                                    navController.navigateTo(route)
                                }
                            )
                        }

                        composable<Routes.AddSubscription> {
                            AddSubscriptionScreenRoot(
                                contentPadding = innerPadding,
                                onNavigateTo = { route ->
                                    navController.navigateTo(route)
                                }
                            )
                        }
                    }
                }
            }


            val expenseLabel = UiText.StringResourceId(Res.string.expenses).asString()
            val incomeLabel = UiText.StringResourceId(Res.string.income).asString()
            val cardLabel = UiText.StringResourceId(Res.string.card).asString()

            if (showAddFabMenu) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.background.copy(alpha = 0.3f))
                        .clickable {
                            showAddFabMenu = false
                        }
                )
            }
            AddFabMenu(
                showMenu = showAddFabMenu,
                modifier = Modifier.padding(innerPadding).align(Alignment.BottomCenter)
            ) { fabItem ->
                showAddFabMenu = false

                when (fabItem.label) {
                    expenseLabel -> {
                        navController.navigateTo(Routes.AddTransaction(false))
                    }

                    incomeLabel -> {
                        navController.navigateTo(Routes.AddTransaction(true))
                    }

                    cardLabel -> {
                        navController.navigateTo(Routes.AddCard)
                    }

                    else -> {
                        showAddFabMenu = false
                    }
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

