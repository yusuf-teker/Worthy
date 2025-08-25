package com.yusufteker.worthy.screen.subscription.domain.model

import androidx.compose.runtime.Composable
import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.UiText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.category_game
import worthy.composeapp.generated.resources.category_music
import worthy.composeapp.generated.resources.category_other
import worthy.composeapp.generated.resources.category_software
import worthy.composeapp.generated.resources.category_video

data class Subscription(
    val id: Int = 0,
    val name: String,                    // Netflix, Spotify vb.
    val icon: String,                    // 🎬, 🎵 vb.
    val color: String? = null,                   // HEX kod
    val category: SubscriptionCategory? = null, // Enum ile tip güvenliği
    val customCategoryName: String? = null,     // Eğer kullanıcı kendi kategorisini oluşturursa
    val money: Money = emptyMoney(),
    val startDate: AppDate,
    val endDate: AppDate? = null,
    val scheduledDay: Int? = 1,          // her ay ödeme günü
    val cardId: Int? = null,              // opsiyonel, bağlı olduğu kart
    val isActive: Boolean = false,         // aktif mi?
)

val emptySubscription = Subscription(
    name = "",
    icon = "",
    startDate = AppDate(getCurrentYear(),getCurrentMonth()),
    isActive = true,
    color = null,
    money = emptyMoney()
)

enum class SubscriptionCategory(val resourceKey: String, val defaultIcon: String) {
    VIDEO("category_video", "🎬"),
    MUSIC("category_music", "🎵"),
    SOFTWARE("category_software", "💻"),
    GAME("category_game", "🎮"),
    OTHER("category_other", "📦")
}

@Composable
fun SubscriptionCategory.getDisplayName(): String {
    val res = when(this) {
        SubscriptionCategory.VIDEO -> Res.string.category_video
        SubscriptionCategory.MUSIC -> Res.string.category_music
        SubscriptionCategory.SOFTWARE -> Res.string.category_software
        SubscriptionCategory.GAME -> Res.string.category_game
        SubscriptionCategory.OTHER -> Res.string.category_other
    }
    return UiText.StringResourceId(res).asString()
}

