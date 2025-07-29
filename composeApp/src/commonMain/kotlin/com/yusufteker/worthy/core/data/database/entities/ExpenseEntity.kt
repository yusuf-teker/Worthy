package com.yusufteker.worthy.core.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yusufteker.worthy.core.domain.model.Money

@Entity(
    tableName = "expenses",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.SET_NULL
    )],
    indices = [Index("categoryId")]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val amount: Money,
    val categoryId: Int?,
    val scheduledDay: Int? = null,
    val needType: ExpenseNeedType = ExpenseNeedType.NEED,
    val isFixed: Boolean = false,
    val date: Long,
    val note: String? = null
)

enum class ExpenseNeedType { NEED, WANT, NONE }
