package com.yusufteker.worthy.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object OnboardingGraph : Routes()

    @Serializable
    data object Onboarding : Routes()

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
    data class AddTransaction(val isIncome: Boolean) : Routes() {
        override fun toString() = NAME

        companion object {
            const val NAME = "AddTransaction"
        }
    }


    @Serializable
    data object AddCard : Routes()

    @Serializable
    data object Trends : Routes()

    @Serializable
    data object Settings : Routes()


}