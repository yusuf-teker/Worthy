package com.yusufteker.worthy.core.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.screen.card.data.database.entities.CardEntity

@Entity(
    tableName = "transactions", foreignKeys = [ForeignKey(
        entity = TransactionEntity::class,
        parentColumns = ["id"],
        childColumns = ["relatedTransactionId"],
        onDelete = ForeignKey.SET_NULL
    ), ForeignKey(
        entity = CardEntity::class,
        parentColumns = ["id"],
        childColumns = ["cardId"],
        onDelete = ForeignKey.SET_NULL
    ), ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.SET_NULL
    )], indices = [Index("relatedTransactionId"), Index("cardId"), Index("categoryId")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val originalId: Int = id,
    val name: String,
    val amount: Money,
    val transactionType: TransactionType,
    val categoryId: Int?,
    val cardId: Int?,
    val transactionDate: Long,
    val relatedTransactionId: Int? = null,
    val installmentCount: Int? = null,
    val installmentStartDate: AppDate? = null,
    val refundDate: Long? = null,
    val firstPaymentDate: Long,
    val note: String? = null
)
