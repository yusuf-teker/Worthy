package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear

data class YearMonth(val year: Int, val month: Int)

fun getCurrentYearMonth(): YearMonth{
    return YearMonth(getCurrentYear(), getCurrentMonth())
}

fun YearMonth.getLastMonth(): YearMonth{
    if (this.month > 1){
        return this.copy(month = month - 1)
    }else{
        return this.copy(year = this.year - 1,  12)
    }

}