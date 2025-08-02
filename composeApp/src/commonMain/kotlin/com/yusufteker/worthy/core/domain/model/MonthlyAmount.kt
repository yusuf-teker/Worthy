package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem
import worthy.composeapp.generated.resources.Res

data class MonthlyAmount(
    val month: String,
    val amount: Float
)

data class DashboardMonthlyAmount(
    val yearMonth: YearMonth,
    val amount: List<Money>
)

