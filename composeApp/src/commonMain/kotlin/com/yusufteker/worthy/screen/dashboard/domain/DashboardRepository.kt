package com.yusufteker.worthy.screen.dashboard.domain


import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface  DashboardRepository {

    fun getAllRecurringMonthlyAmount(
        monthCount: Int,
        currentDate: LocalDate
    ):  Flow<DashboardRecurringData>


    fun getAllIncomeMonthlyAmount(
        monthCount: Int,
        currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>>

    fun getAllExpenseMonthlyAmount(
        monthCount: Int,
        currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>>

    fun getAllWishlistMonthlyAmount(
        monthCount: Int,
        currentDate: LocalDate
    ): Flow<List<DashboardMonthlyAmount>>

    fun getExpenseCategories(): Flow<List<Category>>

    suspend fun addPurchase(expense: Transaction)
}
