package com.yusufteker.worthy.screen.settings.domain

import kotlinx.serialization.Serializable

@Serializable
data class IncomeItem(
    val id: String = "",
    val name: String,
    val amount: Float
)