package com.yusufteker.worthy.core.data.database.converters

import androidx.room.TypeConverter
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.screen.subscription.domain.model.SubscriptionCategory

class RoomTypeConverters {
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

    @TypeConverter
    fun fromAppDate(date: AppDate?): Int? {
        return date?.let { it.year * 100 + it.month }
    }

    @TypeConverter
    fun toAppDate(value: Int?): AppDate? {
        return value?.let { AppDate(year = it / 100, month = it % 100) }
    }


    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }

    @TypeConverter
    fun fromCategory(category: SubscriptionCategory?): String? {
        return category?.name
    }

    @TypeConverter
    fun toCategory(value: String?): SubscriptionCategory? {
        return value?.let { SubscriptionCategory.valueOf(it) }
    }

}