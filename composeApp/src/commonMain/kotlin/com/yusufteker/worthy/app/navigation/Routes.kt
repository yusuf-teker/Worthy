package com.yusufteker.worthy.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object OnboardingGraph : Routes()

    @Serializable
    data object Onboarding : Routes()

    @Serializable
    data object UserSetup : Routes()


    @Serializable
    data object MainGraph : Routes()

    @Serializable
    data object Dashboard : Routes()



    @Serializable
    data object WishlistGraph : Routes()
    @Serializable
    data object Wishlist : Routes()

    @Serializable
    data object WishlistAdd : Routes()

    @Serializable
    data class WishlistDetail(val id: Int) : Routes()

    @Serializable
    data object Wallet : Routes()

    @Serializable
    data object AddTransaction: Routes()

    @Serializable
    data object Trends : Routes()

    @Serializable
    data object Settings : Routes()



}