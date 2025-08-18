package com.yusufteker.worthy.screen.wishlist.list.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.data.database.mappers.toExpenseTransaction
import com.yusufteker.worthy.core.domain.getCurrentEpochMillis
import com.yusufteker.worthy.core.domain.repository.SearchHistoryRepository
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.core.presentation.components.SearchResult
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WishlistViewModel(
    private val wishlistRepository: WishlistRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : BaseViewModel<WishlistState>(WishlistState()) {

    init {
        observeWishlistItems()
        observeSearchHistory()
    }

    private fun observeWishlistItems() {
        launchWithLoading {
            wishlistRepository.getAll().onEach { wishlistItems ->
                _state.update { currentState ->
                    currentState.copy(
                        items = wishlistItems
                    )
                }
                performSearch(_state.value.searchText, wishlistItems)

            }.launchIn(viewModelScope)
        }
    }

    private fun observeSearchHistory() {
        launchWithLoading {
            searchHistoryRepository.searchHistory.onEach { history ->
                _state.update { currentState ->
                    currentState.copy(
                        searchHistory = history
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onAction(action: WishlistAction) {

        when (action) {
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
                    val isPurchase = action.isPurchased
                    val purchasedDate = if (isPurchase) getCurrentEpochMillis() else null
                    wishlistRepository.updateIsPurchased(
                        itemId = action.item.id,
                        isPurchased = isPurchase,
                        purchasedTime = purchasedDate
                    )

                    val transaction =
                        wishlistRepository.getById(action.item.id)?.toExpenseTransaction()

                    if (transaction != null) {
                        if (isPurchase) {
                            wishlistRepository.saveExpense(transaction)
                        } else {
                            wishlistRepository.deleteExpense(transaction)
                        }
                    } else {
                        //handleERROR
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