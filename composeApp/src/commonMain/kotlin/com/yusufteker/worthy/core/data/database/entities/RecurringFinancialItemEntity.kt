package com.yusufteker.worthy.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Money
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

enum class ExpenseNeedType { NEED, DESIRE, NONE }

@Entity(tableName = "recurring_financial_item")
data class RecurringFinancialItemEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val groupId: String,
    val name: String,
    val amount: Money,
    val isIncome: Boolean, // true: income, false: expense
    val needType: ExpenseNeedType = ExpenseNeedType.NONE, // sadece giderler için
    val scheduledDay: Int? = 1, // 1-28
    val startDate: AppDate,
    val endDate: AppDate? = null,
    val createdAt: Long = Clock.System.now().epochSeconds
)
