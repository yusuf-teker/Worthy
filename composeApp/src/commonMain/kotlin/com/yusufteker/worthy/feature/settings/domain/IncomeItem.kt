package com.yusufteker.worthy.feature.settings.domain

import kotlinx.serialization.Serializable

@Serializable
data class IncomeItem(
    val id: String = "",
    val name: String,
    val amount: Float
)