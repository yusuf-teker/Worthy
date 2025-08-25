package com.yusufteker.worthy.screen.subscription.domain.model

import androidx.compose.runtime.Composable
import com.yusufteker.worthy.core.presentation.UiText
import kotlinx.serialization.Serializable
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.category_game
import worthy.composeapp.generated.resources.category_music
import worthy.composeapp.generated.resources.category_other
import worthy.composeapp.generated.resources.category_software
import worthy.composeapp.generated.resources.category_video

@Serializable
data class SubscriptionCategory(
    val id: Int,
    val name: String? = null, // Kullanıcı eklediyse burası dolu
    val nameResourceKey: String? = null, // Eklemediyse default kullanılır
    val icon: String,
    val color: String? = null// şuan gereksiz
)

//get default
val SubscriptionCategory.Companion.Default: SubscriptionCategory
    get() = defaultSubscriptionCategories.last()


@Composable
fun SubscriptionCategory?.getDisplayName(): String {
    if (this == null) return ""
    return  nameResourceKey?.let { key ->
        when (key) {
            "category_video" -> Res.string.category_video
            "category_music" -> Res.string.category_music
            "category_software" -> Res.string.category_software
            "category_game" -> Res.string.category_game
            else -> Res.string.category_other
        }
    }?.let { UiText.StringResourceId(it).asString() } ?: name ?: ""
}

val defaultSubscriptionCategories = listOf(
    SubscriptionCategory(1, nameResourceKey = "category_video", icon = "🎬", color =  "#FF5722"),
    SubscriptionCategory(2,  nameResourceKey = "category_music", icon =  "🎵", color =  "#4CAF50"),
    SubscriptionCategory(3,  nameResourceKey = "category_software", icon =  "💻", color =  "#2196F3"),
    SubscriptionCategory(4,  nameResourceKey = "category_game", icon =  "🎮", color =  "#9C27B0"),
    SubscriptionCategory(5,  nameResourceKey = "category_other", icon =  "📦", color =  "#FFC107")
)

val emojiOptionsList = listOf(
    // Video / Film
    "🎬", "📺", "📽️", "🎞️", "🍿", "🎭",

    // Müzik / Ses
    "🎵", "🎶", "🎧", "🎷", "🎸", "🥁", "🎹", "🎤",

    // Yazılım / Teknoloji
    "💻", "🖥️", "⌨️", "🖱️", "📱", "📲", "💾", "🛠️",

    // Oyun
    "🎮", "🕹️", "♟️", "🎲",

    // Kitap / Eğitim
    "📚", "📖", "✏️", "🖋️", "📒", "🎓", "🧠",

    // Diğer eğlence ve aktiviteler
    "🎯", "🏆", "🏅", "🏖️", "🧩", "🪀",

    // Genel simgeler
    "❤️", "✨", "🔥", "⭐", "🌟", "🌈", "🔔", "🎁"
)
