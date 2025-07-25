package com.yusufteker.worthy.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "recurring_financial_item")
data class RecurringFinancialItemEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val groupId: String,

    val name: String,
    val amount: Double,
    val isIncome: Boolean, // true: income, false: expense
    val needType: ExpenseNeedType = ExpenseNeedType.NONE, // sadece giderler için
    val scheduledDay: Int? = 1, // 1-28
    val startMonth: Int,
    val startYear: Int,
    val endMonth: Int?, // null: hâlâ geçerli
    val endYear: Int?,
    val createdAt: Long = Clock.System.now().epochSeconds
)
