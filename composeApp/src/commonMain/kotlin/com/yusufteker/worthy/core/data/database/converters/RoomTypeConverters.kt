package com.yusufteker.worthy.core.data.database.converters

import androidx.room.TypeConverter
import com.yusufteker.worthy.core.data.database.entities.ExpenseNeedType
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class RoomTypeConverters {
    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun fromInstant(instant: Instant): Long = instant.toEpochMilliseconds()

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun toInstant(millis: Long): Instant = Instant.Companion.fromEpochMilliseconds(millis)

    @TypeConverter
    fun fromExpenseNeedType(value: ExpenseNeedType): String = value.name

    @TypeConverter
    fun toExpenseNeedType(value: String): ExpenseNeedType = ExpenseNeedType.valueOf(value)

    @TypeConverter
    fun fromCategoryType(value: CategoryType): String = value.name

    @TypeConverter
    fun toCategoryType(value: String): CategoryType = CategoryType.valueOf(value)

    @TypeConverter
    fun fromMoney(money: Money): String {
        // Örn: "TRY:123.45"
        return "${money.currency.name}:${money.amount}"
    }

    @TypeConverter
    fun toMoney(value: String): Money {
        val parts = value.split(":")
        val currency = Currency.valueOf(parts[0])
        val amount = parts.getOrNull(1)?.toDoubleOrNull() ?: 0.0
        return Money(amount = amount, currency = currency)
    }



}