package com.yusufteker.worthy.screen.wishlist.list.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.getCurrentLocalDateTime
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.SearchHistoryRepository
import com.yusufteker.worthy.core.domain.toEpochMillis
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.core.presentation.components.SearchResult
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WishlistViewModel(
    private val wishlistRepository: WishlistRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : BaseViewModel() {


    private val _state = MutableStateFlow(WishlistState())
    val state: StateFlow<WishlistState> = _state

    init {
        observeWishlistItems()
        observeSearchHistory()
    }

    private fun observeWishlistItems() {
        wishlistRepository.getAll().onEach { wishlistItems ->
            _state.update { currentState ->
                currentState.copy(
                    items = wishlistItems
                )
            }
            performSearch(_state.value.searchText, wishlistItems)

        }.launchIn(viewModelScope)
    }

    private fun observeSearchHistory() {
        searchHistoryRepository.searchHistory.onEach { history ->
            _state.update { currentState ->
                currentState.copy(
                    searchHistory = history
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: WishlistAction){

        when(action){
            WishlistAction.OnClearSearch -> {
                _state.update { it.copy(searchText = "") }
            }
            WishlistAction.OnSearch -> {
                TODO()
            }
            is WishlistAction.OnSearchTextChange -> {
                _state.update { it.copy(searchText = action.text) }
                performSearch(action.text, _state.value.items)

            }

            WishlistAction.OnFabClick -> {
                navigateTo(Routes.WishlistAdd)
            }

            is WishlistAction.OnItemClick -> TODO()
            is WishlistAction.OnItemDelete -> {
                viewModelScope.launch {
                    wishlistRepository.deleteById(action.itemId)
                    Napier.d("Deleted item with ID: ${action.itemId}")
                }
            }

            is WishlistAction.OnIsItemPurchasedChange -> {
                viewModelScope.launch {
                    wishlistRepository.updateIsPurchased(
                        itemId = action.item.id,
                        isPurchased = action.isPurchased,
                        purchasedTime = if (action.isPurchased) getCurrentLocalDateTime().toEpochMillis() else null)

                   val expense =  Expense(
                        id = action.item.id,
                        name = action.item.name,
                        amount = action.item.price,
                        categoryId = action.item.category?.id,
                        date = action.item.addedDate,
                        note = action.item.note
                    )

                    if (action.isPurchased) {
                        wishlistRepository.saveExpense(expense)
                    }else{
                        wishlistRepository.deleteExpense(expense)
                    }


                }
            }
        }
    }

    private fun performSearch(query: String, items: List<WishlistItem>) {
        if (query.isBlank()) {
            _state.update { currentState ->
                currentState.copy(
                    searchResults = emptyList()
                )
            }
        }

        val filteredItems = items.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.note?.contains(query, ignoreCase = true) == true
        }
         val searchResults = filteredItems
            .map {
            SearchResult(
                id = it.id,
                title = it.name,
                description = it.note ?: ""
            )
        }

        searchResults.firstOrNull()?.let {
            addSearchHistory(it.title)
        }

        _state.update { currentState ->
            currentState.copy(
                filteredItems = filteredItems,
                searchResults = searchResults
            )
        }
    }

    fun addSearchHistory(query: String) {
        viewModelScope.launch {
            searchHistoryRepository.addSearchQuery(query)
        }
    }
}