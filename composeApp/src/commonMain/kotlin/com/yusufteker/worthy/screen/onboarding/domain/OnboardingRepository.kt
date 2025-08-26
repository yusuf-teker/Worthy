package com.yusufteker.worthy.screen.onboarding.domain

import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.RecurringItem

interface OnboardingRepository {

    suspend fun addName(name: String)

    suspend fun addSpendingLimit(spendingLimit: Money?)

    suspend fun addMonthlySalary(monthlySalary: RecurringItem.Generic?)

    suspend fun addWeeklyWorkHours(weeklyWorkHours: Int)

    suspend fun addSavingsGoal(savingsGoal: Money?)

    suspend fun addBuyingPriorities(buyingPriorities: List<String>?)

    suspend fun addAppHelpGoals(appHelpGoals: List<String>)

    suspend fun setOnboardingCompleted(completed: Boolean = true)

}
