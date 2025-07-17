package com.yusufteker.worthy.core.domain.model

data class Income(
    val id: Int = 0,
    val name: String,
    val amount: Double,
    val categoryId: Int?,
    val scheduledDay: Int? = null,
    val isFixed: Boolean = false,
    val date: Long,
    val note: String? = null
)
