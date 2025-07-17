package com.yusufteker.worthy.feature.settings.domain

import kotlinx.serialization.Serializable

@Serializable
data class ExpenseItem(
    val id: String = "",
    val name: String,
    val amount: Float,
    val isFixedExpense: Boolean = false,
    val isDesireExpense: Boolean = false
)