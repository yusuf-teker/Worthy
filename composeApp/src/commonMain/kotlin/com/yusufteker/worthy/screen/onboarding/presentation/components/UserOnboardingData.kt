package com.yusufteker.worthy.screen.onboarding.presentation.components

import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney

data class UserOnboardingData(
    val monthlySalary: Money? = emptyMoney(),
    val hasMonthlySalary: Boolean = false,
    val spendingLimit: Money? =  emptyMoney(),
    val hasFixedExpenses: Boolean = false,
    val hasSavingsGoal: Boolean = false,
    val savingGoalMoney: Money? =  emptyMoney(),
    val buyingPrioritys: List<String> = emptyList(),
    val appHelpGoals: List<String> = emptyList(),

    )
