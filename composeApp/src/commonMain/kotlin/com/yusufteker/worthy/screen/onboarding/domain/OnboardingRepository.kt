package com.yusufteker.worthy.screen.onboarding.domain

import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRecurringData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate


interface  OnboardingRepository {

    suspend fun addName(name: String)

    suspend fun addSpendingLimit(spendingLimit: Money?)

    suspend fun addMonthlySalary(monthlySalary: RecurringFinancialItem?)

    suspend fun addSavingsGoal(savingsGoal: Money?)

    suspend fun addBuyingPriorities(buyingPriorities: List<String>?)

    suspend fun addAppHelpGoals(appHelpGoals: List<String>)

    suspend fun setOnboardingCompleted(completed: Boolean = true)


}
