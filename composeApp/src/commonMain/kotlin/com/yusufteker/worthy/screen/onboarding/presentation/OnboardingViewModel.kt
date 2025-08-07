package com.yusufteker.worthy.screen.onboarding.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.createTimestampId
import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingRepository
import com.yusufteker.worthy.screen.onboarding.presentation.components.UserOnboardingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository
) : BaseViewModel() {


    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state

    fun onAction(action: OnboardingAction){
        when(action){
            is OnboardingAction.OnGetStartedClicked -> {
                viewModelScope.launch {
                    if (action.userData.isValid()){
                        action.userData.let {
                            onboardingRepository.addName(it.name)
                            onboardingRepository.addMonthlySalary(
                                monthlySalary = RecurringFinancialItem(
                                    name = it.salaryString,
                                    isIncome = true,
                                    amount = it.monthlySalary!!,
                                    startMonth = getCurrentMonth(),
                                    startYear = getCurrentYear(),
                                    endMonth = null,
                                    endYear = null,
                                    groupId = createTimestampId(),
                                )
                            )
                            onboardingRepository.addSpendingLimit(it.spendingLimit)
                            onboardingRepository.addSavingsGoal(it.savingGoalMoney)

                        }
                    }
                    onboardingRepository.setOnboardingCompleted(true)
                }
            }

        }


    }
}

 fun UserOnboardingData.isValid(): Boolean {
    if (name.isBlank()) return false
    if (hasMonthlySalary && (monthlySalary == null || monthlySalary.amount <= 0)) return false
    if (wantSpendingLimit && (spendingLimit == null || spendingLimit.amount <= 0)) return false
    if (hasSavingsGoal && (savingGoalMoney == null || savingGoalMoney.amount <= 0)) return false
    return true
}
