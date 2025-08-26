package com.yusufteker.worthy.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.presentation.UiText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.desire
import worthy.composeapp.generated.resources.need
import worthy.composeapp.generated.resources.none
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

enum class ExpenseNeedType { NEED, DESIRE, NONE }

fun ExpenseNeedType.toText(): UiText {
    return when (this) {
        ExpenseNeedType.NEED -> UiText.StringResourceId(Res.string.need)
        ExpenseNeedType.DESIRE -> UiText.StringResourceId(Res.string.desire)
        ExpenseNeedType.NONE -> UiText.StringResourceId(Res.string.none)
    }
}

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
    val createdAt: Long = Clock.System.now().epochSeconds,
    val category: Category?
)
