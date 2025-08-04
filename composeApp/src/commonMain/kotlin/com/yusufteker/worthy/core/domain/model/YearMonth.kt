package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear

data class YearMonth(val year: Int, val month: Int)

fun getCurrentYearMonth(): YearMonth{
    return YearMonth(getCurrentYear(), getCurrentMonth())
}
