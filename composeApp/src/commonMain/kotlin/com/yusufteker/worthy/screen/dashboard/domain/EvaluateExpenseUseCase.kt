package com.yusufteker.worthy.screen.dashboard.domain

import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.service.CurrencyConverter

class EvaluateExpenseUseCase {

    operator fun invoke(
        money: Money,
        monthlyIncome: Money,
        monthlyExpense: Money,
        desireBudget: Money,
        monthlyWorkHours: Float,
        selectedCurrencySymbol: String
    ): EvaluationResult {

        val incomeMinusExpense = monthlyIncome.amount - monthlyExpense.amount

        val desirePercent = if (desireBudget.amount == 0.0) {
            -1.0
        } else {
            (money.amount / desireBudget.amount) * 100
        }

        val workHours = when {
            monthlyWorkHours == 0f -> -1.0f // Çalışma süresi yok
            monthlyIncome.amount == 0.0 -> -2.0f // Gelir yok
            else -> (money.amount / (monthlyIncome.amount / monthlyWorkHours)).toFloat()
        }

        val remainingDesire = (desireBudget.amount - money.amount).toInt()
        val monthlyIncomePercent = (money.amount / monthlyIncome.amount) * 100
        val incomeMinusExpensePercent = (money.amount / incomeMinusExpense) * 100

        return EvaluationResult(
            incomePercent = monthlyIncomePercent,
            desirePercent = desirePercent,
            workHours = workHours,
            remainingDesire = remainingDesire,
            currencySymbol = selectedCurrencySymbol,
            incomeMinusExpensePercent = incomeMinusExpensePercent
        )
    }
}
