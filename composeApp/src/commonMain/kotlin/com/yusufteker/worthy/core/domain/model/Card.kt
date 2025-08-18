package com.yusufteker.worthy.core.domain.model

data class Card(
    val id: Int = 0,
    val cardHolderName: String,
    val cardNumber: String,       // 16 haneli kart numarası, saklanırken mutlaka şifrelenmeli
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvv: String,              // 3 veya 4 haneli güvenlik kodu, yine şifrelenmeli
    val nickname: String? = null, // Kullanıcının kart için verdiği isim (örn. "Kredi Kartı 1")
    val cardBrand: CardBrand? = null,   // Visa, Mastercard, vs.
    val note: String? = null,
    val statementDay: Int? = null
)

enum class CardBrand { Visa, Mastercard, Amex, Troy, Unknown }
