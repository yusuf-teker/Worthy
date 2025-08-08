package com.yusufteker.worthy.core.data.database.repository

import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem
import com.yusufteker.worthy.core.domain.repository.RecurringFinancialItemRepository
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingRepository
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager


class OnboardingRepositoryImpl(
    private val userPrefsManager: UserPrefsManager,
    private val onboardingManager: OnboardingManager,
    private val recurringRepository: RecurringFinancialItemRepository,
): OnboardingRepository {

    override suspend fun addName(name: String) {
        userPrefsManager.setUserName(name)
    }

    override suspend fun addSpendingLimit(spendingLimit: Money?) {
        spendingLimit?.let {
            userPrefsManager.setSpendingLimit(it)
        }
    }

    override suspend fun addMonthlySalary(monthlySalary: RecurringFinancialItem?) {
        monthlySalary?.let {
            recurringRepository.add(it)
        }
    }

    override suspend fun addWeeklyWorkHours(weeklyWorkHours: Int) {
        userPrefsManager.setWeeklyWorkHours(weeklyWorkHours)
    }

    override suspend fun addSavingsGoal(savingsGoal: Money?) {
        savingsGoal?.let {
            userPrefsManager.setSavingGoal(it)

        }
    }

    override suspend fun addBuyingPriorities(buyingPriorities: List<String>?) {
        //
    }

    override suspend fun addAppHelpGoals(appHelpGoals: List<String>) {
        //
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        onboardingManager.setOnboardingCompleted(completed)
    }

}