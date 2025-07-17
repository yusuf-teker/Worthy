package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.data.database.entities.ExpenseNeedType

data class Expense(
    val id: Int = 0,
    val name: String,
    val amount: Double,
    val categoryId: Int?,
    val needType: ExpenseNeedType = ExpenseNeedType.NEED,
    val scheduledDay: Int? = null,
    val isFixed: Boolean = false,
    val date: Long,
    val note: String? = null
)