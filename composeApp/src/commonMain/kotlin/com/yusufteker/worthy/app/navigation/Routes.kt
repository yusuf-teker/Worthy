package com.yusufteker.worthy.app.navigation

import com.yusufteker.worthy.core.domain.model.RecurringItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class Routes(
    @Transient val screenName: String = ""
) {

    @Serializable
    data object OnboardingGraph : Routes("OnboardingGraph")

    @Serializable
    data object Onboarding : Routes("Onboarding")

    @Serializable
    data object MainGraph : Routes("MainGraph")

    @Serializable
    data object Dashboard : Routes("Dashboard")

    @Serializable
    data object WishlistGraph : Routes("WishlistGraph")

    @Serializable
    data object Wishlist : Routes("Wishlist")

    @Serializable
    data object WishlistAdd : Routes("WishlistAdd")

    @Serializable
    data class WishlistDetail(val id: Int) : Routes("WishlistDetail")

    @Serializable
    data object Wallet : Routes("Wallet")

    @Serializable
    data class AddTransaction(val isIncome: Boolean = false) : Routes("AddTransaction")

    @Serializable
    data object AddCard : Routes("AddCard")

    @Serializable
    data object CardList : Routes("CardList")

    @Serializable
    data object CardGraph : Routes("CardGraph")


    @Serializable
    data object AnalyticsGraph : Routes("AnalyticsGraph")

    @Serializable
    data object Analytics : Routes("Analytics")

    @Serializable
    data class TransactionDetail(val transactionId: Int): Routes("TransactionDetail")

    @Serializable
    data object Settings : Routes("Settings")

    @Serializable
    data object Back : Routes("Back")

    @Serializable
    data object SubscriptionGraph : Routes("SubscriptionGraph")

    @Serializable
    data object SubscriptionList : Routes("SubscriptionList")

    @Serializable
    data object AddSubscription : Routes("AddSubscription")

    @Serializable
    data class SubscriptionDetail(val subscriptionId: Int): Routes("SubscriptionDetail")

}

fun Routes.isAddTransaction(): Boolean {
    return this.toString().substringBefore("(") == Routes.AddTransaction().screenName
}