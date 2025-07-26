package com.yusufteker.worthy.screen.wishlist.list.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WishlistViewModel(
    private val categoryRepository: CategoryRepository,
    private val userPrefsManager: UserPrefsManager
) : BaseViewModel() {


    private val _state = MutableStateFlow(WishlistState())
    val state: StateFlow<WishlistState> = _state


    fun onAction(action: WishlistAction){

        when(action){
            WishlistAction.OnClearSearch -> {
                _state.update { it.copy(searchText = "") }
            }
            WishlistAction.OnSearch -> {
                viewModelScope.launch {
                    val searchText = _state.value.searchText
                    if (searchText.isNotBlank()) {
                        // Perform search logic here
                        Napier.d("Searching for: $searchText")
                        // Update state with search results if needed
                    } else {
                        Napier.w("Search text is empty, cannot perform search.")
                    }
                }
            }
            is WishlistAction.OnSearchTextChange -> {
                _state.update { it.copy(searchText = action.text) }

            }

            WishlistAction.OnFabClick -> {
                navigateTo(Routes.WishlistAdd)
            }
        }
    }

}