package com.yusufteker.worthy.screen.onboarding.presentation.components

import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.UiText

data class UserOnboardingData(
    val name: String = "",

    val monthlySalary: Money? = emptyMoney(),
    val hasMonthlySalary: Boolean = false,
    val salaryString: String = "",

    val weeklyWorkHours: Int = 0,

    val wantSpendingLimit: Boolean = false,
    val spendingLimit: Money? = emptyMoney(),

    val hasFixedExpenses: Boolean = false,
    val hasSavingsGoal: Boolean = false,

    val savingGoalMoney: Money? = emptyMoney(),
    val buyingPrioritys: List<String> = emptyList(),
    val appHelpGoals: List<String> = emptyList(),

    val validationErrors: UserOnboardingValidationErrors = UserOnboardingValidationErrors()

)

data class UserOnboardingValidationErrors(
    val nameError: UiText? = null,
    val salaryError: UiText? = null,
    val spendingLimitError: UiText? = null,
    val savingsGoalError: UiText? = null,
    val weeklyHourError: UiText? = null,
)

fun UserOnboardingValidationErrors.isAynError(): Boolean {
    return nameError != null || salaryError != null || spendingLimitError != null || savingsGoalError != null || weeklyHourError != null

}