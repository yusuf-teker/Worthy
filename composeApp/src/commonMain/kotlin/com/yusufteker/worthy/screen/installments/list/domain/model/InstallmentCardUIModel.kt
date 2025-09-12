package com.yusufteker.worthy.screen.installments.list.domain.model

import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.screen.card.domain.model.Card

data class InstallmentCardUIModel(
    val transaction: Transaction,
    val card: Card?
)
