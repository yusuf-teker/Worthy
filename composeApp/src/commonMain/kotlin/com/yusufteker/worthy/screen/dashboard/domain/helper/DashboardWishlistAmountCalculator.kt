package com.yusufteker.worthy.screen.dashboard.domain.helper

import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.toLocalDate
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

fun calculateWishlistAmounts(
    sortedItems: List<WishlistItem>,
    monthDates: List<LocalDate>
): List<DashboardMonthlyAmount> {
    val result = LinkedHashMap<Pair<Int, Int>, MutableList<Money>>()
    val activeItems = mutableListOf<Money>()
    var itemIndex = 0

    monthDates.forEach { monthDate ->
        val yearMonth = monthDate.year to monthDate.month.number

        // Add new items that become active this month
        while (itemIndex < sortedItems.size) {
            val itemDate = sortedItems[itemIndex].addedDate.toLocalDate()
            val normalizedItemDate = LocalDate(itemDate.year, itemDate.month.number, 1)

            if (normalizedItemDate <= monthDate) {
                activeItems.add(sortedItems[itemIndex].price)
                itemIndex++
            } else {
                break
            }
        }

        if (activeItems.isNotEmpty()) {
            result.getOrPut(yearMonth) { ArrayList(activeItems.size) }
                .addAll(activeItems)
        }
    }

    return result.map { (yearMonth, amounts) ->
        DashboardMonthlyAmount(
            appDate = AppDate(yearMonth.first, yearMonth.second),
            amount = amounts
        )
    }.sortedWith(compareBy({ it.appDate.year }, { it.appDate.month }))
}
