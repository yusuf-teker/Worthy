package com.yusufteker.worthy.screen.onboarding.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import kotlinx.coroutines.launch
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import org.jetbrains.compose.resources.stringResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.choice_budget_impact
import worthy.composeapp.generated.resources.choice_necessity
import worthy.composeapp.generated.resources.choice_price
import worthy.composeapp.generated.resources.choice_quality
import worthy.composeapp.generated.resources.common_back
import worthy.composeapp.generated.resources.common_next
import worthy.composeapp.generated.resources.error_empty_name
import worthy.composeapp.generated.resources.error_invalid_name
import worthy.composeapp.generated.resources.error_invalid_salary
import worthy.composeapp.generated.resources.error_invalid_savings_goal
import worthy.composeapp.generated.resources.error_invalid_spending_limit
import worthy.composeapp.generated.resources.get_started
import worthy.composeapp.generated.resources.goal_conscious_spending
import worthy.composeapp.generated.resources.goal_decision_support
import worthy.composeapp.generated.resources.goal_limit_spending
import worthy.composeapp.generated.resources.goal_save_more
import worthy.composeapp.generated.resources.goal_savings
import worthy.composeapp.generated.resources.goal_simple_use
import worthy.composeapp.generated.resources.hint_enter_name
import worthy.composeapp.generated.resources.label_name
import worthy.composeapp.generated.resources.label_salary
import worthy.composeapp.generated.resources.onboarding_q0_welcome
import worthy.composeapp.generated.resources.onboarding_q1_title
import worthy.composeapp.generated.resources.onboarding_q2_title
import worthy.composeapp.generated.resources.onboarding_q4_title
import worthy.composeapp.generated.resources.onboarding_q5_title
import worthy.composeapp.generated.resources.onboarding_q6_title

@Composable
fun UserFormPager(
    modifier: Modifier = Modifier,
    onGetStarted: (UserOnboardingData) -> Unit,
) {


    val pagerState = rememberPagerState(pageCount = { 6})
    val coroutineScope = rememberCoroutineScope()

    val defaultName = stringResource(Res.string.label_salary)

    var userOnboardingData by remember {
        mutableStateOf(
            UserOnboardingData(salaryString = defaultName)
        )
    }

    var errorMessage by remember { mutableStateOf<UiText?>(null) }

    fun validatePage(page: Int): Boolean {
        errorMessage = when (page) {
            0 -> when {
                userOnboardingData.name.isBlank() -> UiText.StringResourceId(Res.string.error_empty_name)
                userOnboardingData.name.length < 3 -> UiText.StringResourceId(Res.string.error_invalid_name)
                else -> null
            }
            1 -> if (userOnboardingData.hasMonthlySalary && (userOnboardingData.monthlySalary?.amount ?: 0.0) <= 0.0)
                UiText.StringResourceId(Res.string.error_invalid_salary)
            else null
            2 -> if (userOnboardingData.wantSpendingLimit && (userOnboardingData.spendingLimit?.amount ?: 0.0) <= 0.0)
                UiText.StringResourceId(Res.string.error_invalid_spending_limit)
            else null
            3 -> if (userOnboardingData.hasSavingsGoal && (userOnboardingData.savingGoalMoney?.amount ?: 0.0) <= 0.0)
                UiText.StringResourceId(Res.string.error_invalid_savings_goal)
            else null
            else -> null
        }
        return errorMessage == null
    }

    Column(modifier = modifier.fillMaxSize()) {

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
           Column(modifier = modifier.weight(1f), verticalArrangement = Arrangement.Center) {

               when(page)
               {
                   0 -> {
                       Text(
                           UiText.StringResourceId(Res.string.onboarding_q0_welcome) .asString(),
                           style = AppTypography.titleMedium,
                           modifier = Modifier.fillMaxWidth()
                       )
                       Spacer(Modifier.height(16.dp))
                       QuestionInput(
                           text = userOnboardingData.name,
                           onTextChange = {
                               userOnboardingData = userOnboardingData.copy(name = it)
                           },
                           placeholder = UiText.StringResourceId(Res.string.hint_enter_name).asString()
                       )
                   }
                   1 ->{

                       QuestionToggleMoneyInput(
                           question =  UiText.StringResourceId(id = Res.string.onboarding_q1_title),
                           label = UiText.StringResourceId(id = Res.string.label_salary),
                           money = userOnboardingData.monthlySalary,
                           onMoneyChange = {
                               userOnboardingData = userOnboardingData.copy(monthlySalary = it)
                           },
                           checked = userOnboardingData.hasMonthlySalary,
                           onCheckedChange = {
                               userOnboardingData = userOnboardingData.copy(hasMonthlySalary = it)
                               if (!it) userOnboardingData = userOnboardingData.copy(monthlySalary =  emptyMoney())
                           },

                       )

                   }
                   2 -> { // TODO HARCAMA LIMITI EKLENECEK ANA FONKSIYONLARA
                       QuestionToggleMoneyInput(
                           question =  UiText.StringResourceId(id = Res.string.onboarding_q2_title),
                           label = UiText.StringResourceId(id = Res.string.goal_limit_spending),
                           money = userOnboardingData.spendingLimit,
                           onMoneyChange = {
                               userOnboardingData = userOnboardingData.copy(monthlySalary = it)
                           },
                           checked = userOnboardingData.wantSpendingLimit,
                           onCheckedChange = {
                               userOnboardingData = userOnboardingData.copy(wantSpendingLimit = it)
                               if (!it) userOnboardingData = userOnboardingData.copy(spendingLimit =  emptyMoney())
                           }
                       )

                   }

                   3 -> {
                       QuestionToggleMoneyInput(
                           question =  UiText.StringResourceId(id = Res.string.onboarding_q4_title),
                           label = UiText.StringResourceId(id = Res.string.goal_savings),
                           money = userOnboardingData.savingGoalMoney,
                           onMoneyChange = {
                               userOnboardingData = userOnboardingData.copy(savingGoalMoney = it)
                           },
                           checked = userOnboardingData.hasSavingsGoal,
                           onCheckedChange = {
                               userOnboardingData = userOnboardingData.copy(hasSavingsGoal = it)
                               if (!it) userOnboardingData = userOnboardingData.copy(savingGoalMoney =  emptyMoney())
                           }
                       )

                   }
                   4 -> QuestionFlowRow(
                       question = UiText.StringResourceId(id = Res.string.onboarding_q5_title),
                       options = listOf(
                           UiText.StringResourceId(id = Res.string.choice_price).asString(),
                           UiText.StringResourceId(id = Res.string.choice_quality).asString(),
                           UiText.StringResourceId(id = Res.string.choice_necessity).asString(),
                           UiText.StringResourceId(id = Res.string.choice_budget_impact).asString()
                       ),
                       selectedOptions = userOnboardingData.buyingPrioritys,
                       maxSelection = 2,
                       onSelectionChange = { newList ->
                           userOnboardingData = userOnboardingData.copy(buyingPrioritys = newList)
                       }
                   )
                   5 -> QuestionFlowRow(
                       question =  UiText.StringResourceId(id = Res.string.onboarding_q6_title),
                       options = listOf(
                           UiText.StringResourceId(id = Res.string.goal_limit_spending).asString(),
                           UiText.StringResourceId(id = Res.string.goal_save_more).asString(),
                           UiText.StringResourceId(id = Res.string.goal_conscious_spending).asString(),
                           UiText.StringResourceId(id = Res.string.goal_decision_support).asString(),
                           UiText.StringResourceId(id = Res.string.goal_simple_use).asString()
                       ),
                       maxSelection = 2,
                       selectedOptions = userOnboardingData.appHelpGoals,
                       onSelectionChange = {userOnboardingData =  userOnboardingData.copy(appHelpGoals = it) }
                   )
               }

               errorMessage?.let {
                   Text(
                       it.asString(),
                       color = AppColors.error,
                   )
               }
           }

        }

        Spacer(Modifier.height(16.dp))

        // Pager Indicator (Custom implementation)
        PagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            activeColor = AppColors.primary
        )

        Spacer(Modifier.height(16.dp))

        // Navigation Buttons
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (pagerState.currentPage > 0) {
                Button(onClick = {
                    coroutineScope.launch {
                        errorMessage = null
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }) {
                    Text(text = UiText.StringResourceId(Res.string.common_back).asString())
                }
            } else {
                Spacer(modifier = Modifier.width(64.dp))
            }

            if (pagerState.currentPage < pagerState.pageCount - 1) {
                if (pagerState.currentPage < pagerState.pageCount - 1) {
                    Button(onClick = {
                        if (validatePage(pagerState.currentPage)) {
                            errorMessage = null
                            coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        }
                    }) {
                        Text(text = UiText.StringResourceId(Res.string.common_next).asString())
                    }
                }

            } else {
                Button(
                    onClick = {
                        onGetStarted(userOnboardingData)
                    }
                ) {
                    Text(text = UiText.StringResourceId(id = Res.string.get_started).asString())
                }
            }
        }
    }

}

// Custom Pager Indicator implementation
@Composable
fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = AppColors.primary,
    inactiveColor: Color = AppColors.onSurface.copy(alpha = 0.3f),
    indicatorSize: Dp = 8.dp,
    spacing: Dp = 4.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        repeat(pagerState.pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(indicatorSize)
                    .clip(CircleShape)
                    .background(
                        color = if (index == pagerState.currentPage) activeColor else inactiveColor
                    )
            )
        }
    }
}

@Composable
fun QuestionInput(
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String? = null
) {
    OutlinedTextField(

        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = { value ->
            val filtered = value.filter { it.isLetter() || it.isWhitespace() }
            onTextChange(filtered)
       },
        placeholder = { placeholder?.let { Text(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}


@Composable
fun QuestionToggleMoneyInput(
    question: UiText,
    label: UiText,
    modifier: Modifier = Modifier,
    money: Money? = emptyMoney(),
    onMoneyChange: (Money?) -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {



    Column {
        FlowRow(
            verticalArrangement = Arrangement.Center,
            itemVerticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(question.asString(), style = AppTypography.titleMedium)
            Spacer(Modifier.width(8.dp))
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
        Spacer(Modifier.height(8.dp))
        val targetAlpha = if (checked) 1f else 0f
        val animatedAlpha by animateFloatAsState(
            targetValue = targetAlpha,
            animationSpec = tween(durationMillis = 600)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(animatedAlpha)
        ) {
            MoneyInput(
                money = money,
                onValueChange = {
                    onMoneyChange.invoke(it)
                },
                label = label,
            )
        }
    }
}

@Composable
fun QuestionFlowRow(
    question: UiText,
    options: List<String>,
    selectedOptions: List<String>,
    maxSelection: Int = options.size,
    onSelectionChange: (List<String>) -> Unit
) {
    Column {
        Text(question.asString(), style = AppTypography.titleMedium)
        Spacer(Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            options.forEach { option ->
                val isSelected = selectedOptions.contains(option)

                Surface(
                    color = if (isSelected) AppColors.primary else AppColors.surfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            val updatedSelection = when {
                                isSelected -> selectedOptions - option // çıkar
                                selectedOptions.size < maxSelection -> selectedOptions + option // ekle
                                else -> selectedOptions // limit aşıldıysa dokunma
                            }
                            onSelectionChange(updatedSelection)
                        }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = option,
                            color = if (isSelected) AppColors.onPrimary else AppColors.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

