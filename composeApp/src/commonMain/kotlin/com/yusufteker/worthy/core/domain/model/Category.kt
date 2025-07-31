package com.yusufteker.worthy.core.domain.model

import kotlin.time.ExperimentalTime


data class Category @OptIn(ExperimentalTime::class) constructor(
    val id: Int = 0,
    val name: String,
    val type: CategoryType,
    val createdAt: Long = kotlin.time.Clock.System.now().toEpochMilliseconds(),
    val userCreated: Boolean = true,
    val icon: String? = null,
    val colorHex: String? = null
)


enum class CategoryType {
    INCOME,
    EXPENSE,
    WISHLIST
}

val defaultCategories = listOf(
    // Gider (EXPENSE)
    Category(id = 1, name = "Alışveriş", icon = "🛍️", type = CategoryType.EXPENSE),
    Category(id = 2, name = "Yiyecek", icon = "🍔", type = CategoryType.EXPENSE),
    Category(id = 3, name = "Ulaşım", icon = "🚗", type = CategoryType.EXPENSE),
    Category(id = 4, name = "Eğlence", icon = "🎮", type = CategoryType.EXPENSE),
    Category(id = 5, name = "Sağlık", icon = "💊", type = CategoryType.EXPENSE),
    Category(id = 6, name = "Kira", icon = "🏠", type = CategoryType.EXPENSE),
    Category(id = 7, name = "Faturalar", icon = "💡", type = CategoryType.EXPENSE),
    Category(id = 8, name = "Abonelikler", icon = "📺", type = CategoryType.EXPENSE),
    Category(id = 9, name = "Kredi Kartı", icon = "💳", type = CategoryType.EXPENSE),
    Category(id = 10, name = "Çocuk", icon = "🧸", type = CategoryType.EXPENSE),
    Category(id = 11, name = "Evcil Hayvan", icon = "🐶", type = CategoryType.EXPENSE),
    Category(id = 12, name = "Hediye", icon = "🎁", type = CategoryType.EXPENSE),
    Category(id = 13, name = "Kıyafet", icon = "👗", type = CategoryType.EXPENSE),
    Category(id = 14, name = "Kafe/Restoran", icon = "🍽️", type = CategoryType.EXPENSE),
    Category(id = 15, name = "Diğer", icon = "❓", type = CategoryType.EXPENSE),

    // Gelir (INCOME)
    Category(id = 16, name = "Maaş", icon = "💰", type = CategoryType.INCOME),
    Category(id = 17, name = "Prim", icon = "📈", type = CategoryType.INCOME),
    Category(id = 18, name = "Bonus", icon = "🏅", type = CategoryType.INCOME),
    Category(id = 19, name = "İkramiye", icon = "🎉", type = CategoryType.INCOME),
    Category(id = 20, name = "Kira Geliri", icon = "🏢", type = CategoryType.INCOME),
    Category(id = 21, name = "Yatırım", icon = "📊", type = CategoryType.INCOME),
    Category(id = 22, name = "Birikim Faizi", icon = "💵", type = CategoryType.INCOME),
    Category(id = 23, name = "Serbest Çalışma", icon = "💻", type = CategoryType.INCOME),
    Category(id = 24, name = "Satış Geliri", icon = "🛒", type = CategoryType.INCOME),
    Category(id = 25, name = "Diğer", icon = "❓", type = CategoryType.INCOME),

    // Wishlist (WISHLIST)
    Category(id = 26, name = "Teknoloji", icon = "📱", type = CategoryType.WISHLIST),
    Category(id = 27, name = "Moda", icon = "🕶️", type = CategoryType.WISHLIST),
    Category(id = 28, name = "Kitap", icon = "📚", type = CategoryType.WISHLIST),
    Category(id = 29, name = "Mobilya", icon = "🪑", type = CategoryType.WISHLIST),
    Category(id = 30, name = "Seyahat", icon = "✈️", type = CategoryType.WISHLIST)
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
