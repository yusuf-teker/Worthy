package com.yusufteker.worthy.screen.subscription.domain.model

import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney

data class Subscription(
    val id: Int = 0,
    val name: String,                    // Netflix, Spotify vb.
    val icon: String,                    // 🎬, 🎵 vb.
    val color: String? = null,           // HEX kod
    val category: SubscriptionCategory? = null,         // SubscriptionCategoryEntity.id
    val money: Money = emptyMoney(),
    val startDate: AppDate,
    val endDate: AppDate? = null,
    val scheduledDay: Int? = 1,          // her ay ödeme günü
    val cardId: Int? = null,             // opsiyonel, bağlı olduğu kart
    val isActive: Boolean = false        // aktif mi?
)

val emptySubscription = Subscription(
    name = "",
    icon = "",
    startDate = AppDate(getCurrentYear(),getCurrentMonth()),
    isActive = true,
    color = null,
    money = emptyMoney()
)
