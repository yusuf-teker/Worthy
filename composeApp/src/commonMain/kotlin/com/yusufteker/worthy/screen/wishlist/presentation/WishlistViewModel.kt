package com.yusufteker.worthy.screen.wishlist.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.model.startDate
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.ExpenseRepository
import com.yusufteker.worthy.core.domain.repository.IncomeRepository
import com.yusufteker.worthy.core.domain.repository.RecurringFinancialItemRepository
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import com.yusufteker.worthy.screen.settings.presentation.SettingsAction
import com.yusufteker.worthy.screen.settings.presentation.SettingsState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.component1
import kotlin.collections.component2

class WishlistViewModel(
) : BaseViewModel() {


    private val _state = MutableStateFlow(WishlistState())
    val state: StateFlow<WishlistState> = _state


    fun onAction(action: WishlistAction){

    }

}