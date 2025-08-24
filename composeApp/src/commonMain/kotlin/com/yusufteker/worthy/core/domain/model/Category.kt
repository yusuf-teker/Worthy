package com.yusufteker.worthy.core.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.yusufteker.worthy.core.presentation.UiText
import org.jetbrains.compose.resources.StringResource

import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.category_bills
import worthy.composeapp.generated.resources.category_bonus
import worthy.composeapp.generated.resources.category_books
import worthy.composeapp.generated.resources.category_children
import worthy.composeapp.generated.resources.category_clothing
import worthy.composeapp.generated.resources.category_credit_card
import worthy.composeapp.generated.resources.category_dining
import worthy.composeapp.generated.resources.category_entertainment
import worthy.composeapp.generated.resources.category_fashion
import worthy.composeapp.generated.resources.category_food
import worthy.composeapp.generated.resources.category_freelance
import worthy.composeapp.generated.resources.category_furniture
import worthy.composeapp.generated.resources.category_gift
import worthy.composeapp.generated.resources.category_health
import worthy.composeapp.generated.resources.category_investment
import worthy.composeapp.generated.resources.category_other_expense
import worthy.composeapp.generated.resources.category_other_income
import worthy.composeapp.generated.resources.category_pets
import worthy.composeapp.generated.resources.category_rent
import worthy.composeapp.generated.resources.category_rent_income
import worthy.composeapp.generated.resources.category_reward
import worthy.composeapp.generated.resources.category_salary
import worthy.composeapp.generated.resources.category_sales_income
import worthy.composeapp.generated.resources.category_saving_interest
import worthy.composeapp.generated.resources.category_shopping
import worthy.composeapp.generated.resources.category_subscriptions
import worthy.composeapp.generated.resources.category_technology
import worthy.composeapp.generated.resources.category_transport
import worthy.composeapp.generated.resources.category_travel
import kotlin.time.ExperimentalTime

data class Category @OptIn(ExperimentalTime::class) constructor(
    val id: Int = 0,
    val name: String,
    val nameResourceKey: String? = null, // String olarak resource key
    val type: CategoryType,
    val createdAt: Long = kotlin.time.Clock.System.now().toEpochMilliseconds(),
    val userCreated: Boolean = true,
    val icon: String? = null,
    val colorHex: String? = null,
)

enum class CategoryType {
    INCOME, EXPENSE, WISHLIST
}


val categoryColorPalette = listOf(
    Color(0xFF4CAF50),
    Color(0xFFF44336),
    Color(0xFFFFC107),
    Color(0xFF2196F3),
    Color(0xFF9C27B0),
    Color(0xFFFF5722),
    Color(0xFF3F51B5),
    Color(0xFF00BCD4),
    Color(0xFFE91E63),
    Color(0xFF8BC34A)
)

// index'e göre renk seç
fun getColorByIndex(index: Int): Color {
    return categoryColorPalette[index % categoryColorPalette.size]
}


@Composable
fun getResourceByKey(key: String): StringResource {
    return when (key) {
        "category_shopping" -> Res.string.category_shopping
        "category_food" -> Res.string.category_food
        "category_transport" -> Res.string.category_transport
        "category_entertainment" -> Res.string.category_entertainment
        "category_health" -> Res.string.category_health
        "category_rent" -> Res.string.category_rent
        "category_bills" -> Res.string.category_bills
        "category_subscriptions" -> Res.string.category_subscriptions
        "category_credit_card" -> Res.string.category_credit_card
        "category_children" -> Res.string.category_children
        "category_pet" -> Res.string.category_pets
        "category_gift" -> Res.string.category_gift
        "category_clothing" -> Res.string.category_clothing
        "category_dining" -> Res.string.category_dining
        "category_other_expense" -> Res.string.category_other_expense
        "category_salary" -> Res.string.category_salary
        "category_bonus" -> Res.string.category_bonus
        "category_reward" -> Res.string.category_reward
        "category_rental_income" -> Res.string.category_rent_income
        "category_investment" -> Res.string.category_investment
        "category_saving_interest" -> Res.string.category_saving_interest
        "category_freelance" -> Res.string.category_freelance
        "category_sales_income" -> Res.string.category_sales_income
        "category_other_income" -> Res.string.category_other_income
        "category_technology" -> Res.string.category_technology
        "category_fashion" -> Res.string.category_fashion
        "category_books" -> Res.string.category_books
        "category_furniture" -> Res.string.category_furniture
        "category_travel" -> {
            Res.string.category_travel
        }

        else -> Res.string.category_other_expense
    }
}

@Composable
fun Category.getNameResource(): String {
    return if (nameResourceKey != null && nameResourceKey.isNotEmpty()) {
        UiText.StringResourceId(getResourceByKey(nameResourceKey)).asString()
    } else {
        name
    }
}

val defaultCategories = listOf(
    // Gider (EXPENSE)
    Category(
        id = 1,
        name = "Alışveriş",
        icon = "🛍️",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_shopping",
        userCreated = false
    ), Category(
        id = 2,
        name = "Yiyecek",
        icon = "🍔",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_food",
        userCreated = false
    ), Category(
        id = 3,
        name = "Ulaşım",
        icon = "🚗",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_transport",
        userCreated = false
    ), Category(
        id = 4,
        name = "Eğlence",
        icon = "🎮",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_entertainment",
        userCreated = false
    ), Category(
        id = 5,
        name = "Sağlık",
        icon = "💊",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_health",
        userCreated = false
    ), Category(
        id = 6,
        name = "Kira",
        icon = "🏠",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_rent",
        userCreated = false
    ), Category(
        id = 7,
        name = "Faturalar",
        icon = "💡",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_bills",
        userCreated = false
    ), Category(
        id = 8,
        name = "Abonelikler",
        icon = "📺",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_subscriptions",
        userCreated = false
    ), Category(
        id = 9,
        name = "Kredi Kartı",
        icon = "💳",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_credit_card",
        userCreated = false
    ), Category(
        id = 10,
        name = "Çocuk",
        icon = "🧸",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_children",
        userCreated = false
    ), Category(
        id = 11,
        name = "Evcil Hayvan",
        icon = "🐶",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_pet",
        userCreated = false
    ), Category(
        id = 12,
        name = "Hediye",
        icon = "🎁",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_gift",
        userCreated = false
    ), Category(
        id = 13,
        name = "Kıyafet",
        icon = "👗",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_clothing",
        userCreated = false
    ), Category(
        id = 14,
        name = "Kafe/Restoran",
        icon = "🍽️",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_dining",
        userCreated = false
    ), Category(
        id = 15,
        name = "Diğer",
        icon = "❓",
        type = CategoryType.EXPENSE,
        nameResourceKey = "category_other_expense",
        userCreated = false
    ),

    // Gelir (INCOME)
    Category(
        id = 16,
        name = "Maaş",
        icon = "💰",
        type = CategoryType.INCOME,
        nameResourceKey = "category_salary",
        userCreated = false
    ), Category(
        id = 17,
        name = "Prim",
        icon = "📈",
        type = CategoryType.INCOME,
        nameResourceKey = "category_bonus",
        userCreated = false
    ), Category(
        id = 18,
        name = "Bonus",
        icon = "🏅",
        type = CategoryType.INCOME,
        nameResourceKey = "category_bonus",
        userCreated = false
    ), Category(
        id = 19,
        name = "İkramiye",
        icon = "🎉",
        type = CategoryType.INCOME,
        nameResourceKey = "category_reward",
        userCreated = false
    ), Category(
        id = 20,
        name = "Kira Geliri",
        icon = "🏢",
        type = CategoryType.INCOME,
        nameResourceKey = "category_rental_income",
        userCreated = false
    ), Category(
        id = 21,
        name = "Yatırım",
        icon = "📊",
        type = CategoryType.INCOME,
        nameResourceKey = "category_investment",
        userCreated = false
    ), Category(
        id = 22,
        name = "Birikim Faizi",
        icon = "💵",
        type = CategoryType.INCOME,
        nameResourceKey = "category_saving_interest",
        userCreated = false
    ), Category(
        id = 23,
        name = "Serbest Çalışma",
        icon = "💻",
        type = CategoryType.INCOME,
        nameResourceKey = "category_freelance",
        userCreated = false
    ), Category(
        id = 24,
        name = "Satış Geliri",
        icon = "🛒",
        type = CategoryType.INCOME,
        nameResourceKey = "category_sales_income",
        userCreated = false
    ), Category(
        id = 25,
        name = "Diğer",
        icon = "❓",
        type = CategoryType.INCOME,
        nameResourceKey = "category_other_income",
        userCreated = false
    ),

    // Wishlist (WISHLIST)
    Category(
        id = 26,
        name = "Teknoloji",
        icon = "📱",
        type = CategoryType.WISHLIST,
        nameResourceKey = "category_technology",
        userCreated = false
    ), Category(
        id = 27,
        name = "Moda",
        icon = "🕶️",
        type = CategoryType.WISHLIST,
        nameResourceKey = "category_fashion",
        userCreated = false
    ), Category(
        id = 28,
        name = "Kitap",
        icon = "📚",
        type = CategoryType.WISHLIST,
        nameResourceKey = "category_books",
        userCreated = false
    ), Category(
        id = 29,
        name = "Mobilya",
        icon = "🪑",
        type = CategoryType.WISHLIST,
        nameResourceKey = "category_furniture",
        userCreated = false
    ), Category(
        id = 30,
        name = "Seyahat",
        icon = "✈️",
        type = CategoryType.WISHLIST,
        nameResourceKey = "category_travel",
        userCreated = false
    )
)

val emojiOptions = listOf(
    // Genel
    "💼", "📦", "🗂️", "📅", "📌", "🧾", "🗃️", "🏷️",

    // Gelir (Income)
    "💰", "💵", "💳", "🏦", "📈", "🧧", "💎", "🎉",

    // Gider (Expense)
    "🛒", "📉", "💸", "🧺", "🏠",

    // Alışveriş / Wishlist
    "🛍️", "🎁", "👕", "👠", "📱", "💻", "🎧", "⌚",

    // Ulaşım / Yolculuk
    "🚗", "🚌", "🚕", "🚆", "✈️", "⛽", "🛞", "🗺️",

    // Eğlence
    "🎮", "🎬", "🎶", "📚", "🍿", "🎡", "🎤", "🎭",

    // Yiyecek/İçecek
    "🍔", "🍕", "🍟", "🍣", "🍩", "🍺", "☕",

    // Tatil / Gezi
    "🏖️", "🏝️", "🗽", "🌄", "🌍", "🧳", "📸",

    // Sağlık
    "🏥", "💊", "🩺", "🧬", "🧘", "🦷", "🩹", "🩸",

    // Eğitim
    "🎓", "📖", "✏️", "🖋️", "📒", "🧠", "🏫",

    // Ev / Kira
    "🛋️", "🛏️", "🚿", "🏡", "🏢", "🔑", "💡",

    // İş / Ofis
    "🖥️", "📠", "🖨️", "📋", "📎", "📝", "📂", "📊",

    // Birikim / Hedefler
    "🎯", "🏆", "🪙", "🧿", "🔒", "🎖️",

    // Duygusal / Diğer
    "❤️", "✨", "🔥", "⭐", "🌈", "🌟", "🤞", "🔔"
)
