package com.yusufteker.worthy.screen.subscription.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.screen.card.data.database.entities.CardEntity

@Entity(
    tableName = "subscriptions",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.Companion.SET_NULL // kart silinirse subscription boş kalsın
        )
    ]
)
data class SubscriptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: String,
    val color: String?,                   // HEX kod
    val category: String? = null, // enum -> converter ile string saklanacak
    val money: Money = emptyMoney(),
    val startDate: AppDate,
    val endDate: AppDate? = null,
    val scheduledDay: Int? = 1,
    val cardId: Int? = null
)