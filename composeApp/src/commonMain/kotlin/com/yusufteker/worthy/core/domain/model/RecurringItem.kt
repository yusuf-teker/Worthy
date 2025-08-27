package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.data.database.entities.ExpenseNeedType
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

sealed class RecurringItem {
    abstract val id: Int
    abstract val groupId: String
    abstract val name: String
    abstract val amount: Money
    abstract val isIncome: Boolean
    abstract val startDate: AppDate
    abstract val endDate: AppDate?

    abstract val scheduledDay: Int?

    abstract val category: Category?


    // 🔁 Ortak Transaction üretim fonksiyonu
    fun toTransaction(forDate: AppDate): Transaction {
        return Transaction(
            id = 0,
            name = name,
            amount = amount,
            transactionType = if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
            categoryId = null,
            cardId = (this as? Subscription)?.cardId, // sadece subscription’da dolu olur
            transactionDate = forDate.toEpochMillis(),
            note = "Generated from recurring"
        )
    }

    data class Generic(
        override val id: Int = 0,
        override val groupId: String,
        override val name: String,
        override val amount: Money = emptyMoney(),
        override val isIncome: Boolean,
        override val startDate: AppDate,
        override val endDate: AppDate? = null,
        override val scheduledDay: Int? = 1,
        val needType: ExpenseNeedType = ExpenseNeedType.NONE,
        override val category: Category? = null

        ) : RecurringItem()

    @Serializable
    data class Subscription(
        override val id: Int = 0,
        override val name: String,
        override val groupId: String, // kullanıalcağı zaman createTime uniequ id ile olustur yoksa
        override val amount: Money = emptyMoney(),
        override val isIncome: Boolean = false, // abonelikler genelde gider
        override val startDate: AppDate,
        override val endDate: AppDate? = null,

        val cardId: Int? = null,
        val icon: String = "📦",
        val colorHex: String? = null,
        override val scheduledDay: Int? = 1,
        override val category: Category? = null

        ) : RecurringItem()
}
fun RecurringItem.Subscription.isActive(currentDate: AppDate = getCurrentAppDate()): Boolean {
    return currentDate >= startDate && (endDate == null || currentDate <= endDate)
}
val emptySubscription = RecurringItem.Subscription(
    name = "",
    icon = "",
    startDate = AppDate(getCurrentYear(),getCurrentMonth()),
    colorHex = null,
    amount = emptyMoney(),
    groupId = ""

)


fun RecurringItem.Generic.startDate(): LocalDate =
    LocalDate(year = startDate.year, month = startDate.month, day = 1)

fun RecurringItem.Generic.endDate(): LocalDate? =
    if (endDate?.month != null && endDate.month != null) LocalDate(
        year = endDate.year,
        month = endDate.month,
        day = 1
    )
    else null

fun RecurringItem.Generic.isValidFor(year: Int, month: Int): Boolean {
    val start = this.startDate.year * 100 + this.startDate.month

    val end = if (this.endDate?.year != null && this.endDate.month != null) {
        this.endDate.year * 100 + this.endDate.month!!
    } else { //
        getCurrentAppDate().let { it.year * 100 + it.month } // 🔁 Şu anki tarih
    }
    val given = year * 100 + month

    return given in start..end
}


@OptIn(ExperimentalTime::class)
fun generateMonthlyAmounts(
    isIncome: Boolean? = null,
    items: List<RecurringItem.Generic>,
    referenceDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    monthCount: Int
): List<DashboardMonthlyAmount> {
    val months = getRecentMonths(referenceDate, monthCount)

    return months.map { (year, month) ->
        val validItems = items.filter {
            it.isValidFor(
                year, month
            ) && (if (isIncome != null) it.isIncome == isIncome else true)
        }
        DashboardMonthlyAmount(
            appDate = AppDate(year = year, month = month),
            amount = validItems.map { it.amount ?: emptyMoney() })
    }
}

fun getRecentMonths(currentDate: LocalDate, count: Int): List<Pair<Int, Int>> {
    val months = mutableListOf<Pair<Int, Int>>()
    var year = currentDate.year
    var month = currentDate.month.number

    repeat(count) {
        months.add(Pair(year, month))
        month -= 1
        if (month == 0) {
            month = 12
            year -= 1
        }
    }

    return months.reversed() // eskiden yeniye doğru
}

val items2 = listOf(
    RecurringItem.Generic(
        id = 1,
        groupId = "g1",
        name = "Maaş",
        amount = Money(10000.0, Currency.TRY),
        isIncome = true,
        startDate = AppDate(2023, 1),
    ), RecurringItem.Generic(
        id = 2,
        groupId = "g2",
        name = "Kira",
        amount = Money(3000.0, Currency.TRY),
        isIncome = false,

        startDate = AppDate(2023, 1),
    )
)

