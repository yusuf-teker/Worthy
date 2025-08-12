package com.yusufteker.worthy.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val cardHolderName: String,

    val encryptedCardNumber: String,  // Todo sonra encrypt edilecek

    val expiryMonth: Int,

    val expiryYear: Int,

    val encryptedCvv: String,         //todo sonra encrypt edilecek

    val nickname: String? = null,

    val issuer: String? = null,

    val note: String? = null
)
