package com.yusufteker.worthy.screen.dashboard.presentation.components

import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.screen.dashboard.domain.EvaluationResult

data class BottomSheetUiState(
    val amount: Money = emptyMoney(),
    val selectedCategory: Category? = null,
    val isDropdownExpanded: Boolean = false,
    val isCalculating: Boolean = false,
    val isPurchasing: Boolean = false,
    val evaluationResult: EvaluationResult? = null,
    val categories: List<Category> = emptyList()
)
