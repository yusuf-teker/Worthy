package com.yusufteker.worthy.screen.card.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yusufteker.worthy.screen.card.domain.model.CardBrand

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val cardHolderName: String,

    val encryptedCardNumber: String,  // Todo sonra encrypt edilecek

    val expiryMonth: Int,

    val expiryYear: Int,

    val encryptedCvv: String,         //todo sonra encrypt edilecek

    val nickname: String? = null,

    val cardBrand: CardBrand? = null,

    val note: String? = null
)