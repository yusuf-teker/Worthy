package com.yusufteker.worthy.screen.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.formatTwoDecimals
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import org.jetbrains.compose.ui.tooling.preview.Preview
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.evaluation_desire_budget_percent
import worthy.composeapp.generated.resources.evaluation_expense_income_percent
import worthy.composeapp.generated.resources.evaluation_income_percent
import worthy.composeapp.generated.resources.evaluation_work_hours
import worthy.composeapp.generated.resources.purchase_note_remaining
import worthy.composeapp.generated.resources.purchase_note_title

@Composable
fun PurchaseEvaluationInfoBox(
    incomePercent: Double,
    desireBudgetPercent: Double,
    workHoursRequired: Float,
    incomeMinusExpensePercent: Double,
    remainingDesireBudget: Int,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = UiText.StringResourceId(Res.string.purchase_note_title).asString(),
            style = AppTypography.titleMedium,
            color = AppColors.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = UiText.StringResourceId(
                Res.string.evaluation_income_percent,
                arrayOf(incomePercent.formatTwoDecimals())
            ).asString(),
            style = AppTypography.bodyLarge,
            color = AppColors.onSurface
        )
        Text(
            text = UiText.StringResourceId(
                Res.string.evaluation_expense_income_percent,
                arrayOf(incomeMinusExpensePercent.formatTwoDecimals())
            ).asString(),
            style = AppTypography.bodyLarge,
            color = AppColors.onSurface
        )
        Text(
            text = UiText.StringResourceId(
                Res.string.evaluation_desire_budget_percent,
                arrayOf(desireBudgetPercent.formatTwoDecimals())
            ).asString(),
            style = AppTypography.bodyLarge,
            color = AppColors.onSurface
        )
        Text(
            text = UiText.StringResourceId(
                Res.string.evaluation_work_hours,
                arrayOf(workHoursRequired.formatTwoDecimals())
            ).asString(),
            style = AppTypography.bodyLarge,
            color = AppColors.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = UiText.StringResourceId(
                Res.string.purchase_note_remaining,
                arrayOf(remainingDesireBudget, currencySymbol)
            ).asString(),
            style = AppTypography.bodyLarge,
            color = AppColors.secondary
        )
    }

}

@Preview
@Composable
fun PurchaseEvaluationInfoBoxPreview() {
    PurchaseEvaluationInfoBox(
        incomePercent = 20.0,
        desireBudgetPercent = 30.0,
        workHoursRequired = 5f,
        remainingDesireBudget = 1000,
        incomeMinusExpensePercent = 50.0,
        currencySymbol = "₺",
        modifier = Modifier.padding(16.dp)
    )
}
