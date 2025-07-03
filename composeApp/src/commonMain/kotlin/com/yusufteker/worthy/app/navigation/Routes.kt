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
    data object Dashboard : Routes()

    @Serializable
    data class ItemEvaluation(val productName: String = "", val price: Double = 0.0) : Routes()

    @Serializable
    data object Wishlist : Routes()

    @Serializable
    data object Trends : Routes()

    @Serializable
    data object Settings : Routes()

    @Serializable
    data object MainGraph : Routes()

}