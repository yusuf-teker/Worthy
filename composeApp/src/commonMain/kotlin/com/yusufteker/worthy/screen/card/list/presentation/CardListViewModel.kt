package com.yusufteker.worthy.screen.card.list.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.isAfterOrEqual
import com.yusufteker.worthy.core.domain.isInThisMonth
import com.yusufteker.worthy.core.domain.model.splitInstallmentsByFirstPaymentDate
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.core.presentation.util.sumWithoutCurrencyConverted
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.delete_card_message
import worthy.composeapp.generated.resources.delete_card_title
import worthy.composeapp.generated.resources.delete_subscription_confirm

class CardListViewModel(
    val cardRepository: CardRepository,
    val transactionRepository: TransactionRepository,
) : BaseViewModel<CardListState>(CardListState()) {

    fun onAction(action: CardListAction) {
        when (action) {
            is CardListAction.Init -> {

            }

            is CardListAction.NavigateBack -> {
                navigateBack()
            }

            is CardListAction.AddNewCard -> {
                navigateTo(Routes.AddCard)
            }

            is CardListAction.onCardSelected -> {

                _state.update { currentState ->
                    currentState.copy(
                        selectedCard = action.card
                    )
                }
            }

            is CardListAction.onDeleteCard -> {
                popupManager.showConfirm(
                    title = UiText.StringResourceId(
                        Res.string.delete_card_title,
                        arrayOf(state.value.selectedCard?.nickname ?: "")
                    ),
                    message = UiText.StringResourceId(
                        Res.string.delete_card_message,
                        arrayOf(state.value.selectedCard?.nickname ?: "")
                    ),
                    onConfirm = {
                        launchWithLoading {
                            action.card?.let { card ->
                                cardRepository.deleteCard(card.id)
                                _state.update { currentState ->
                                    currentState.copy(
                                        selectedCard = null
                                    )
                                }
                            }
                        }
                    },
                    confirmLabel = UiText.StringResourceId(Res.string.delete_subscription_confirm),
                    dismissLabel = UiText.StringResourceId(Res.string.cancel)
                )

            }
        }
    }

    init {
        observeCardList()
        observeSelectedCardExpense()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeCardList() {
        launchWithLoading {

            cardRepository.getAll().onEach { cards ->
                _state.update { currentState ->
                    currentState.copy(
                        cards = cards
                    )
                }
            }.launchIn(viewModelScope)

        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeSelectedCardExpense() {
        state.map { it.selectedCard?.id } // selectedCardId
            .flatMapLatest { cardId ->
                if (cardId != null) {
                    transactionRepository.getByCardIdConverted(cardId)
                } else {
                    // boş bir liste döndür
                    flowOf(emptyList())
                }
            }
            .onEach { transactions ->
                // Taksitli işemleri transaction liste çevir ay ay ayır
                val expandedTransactions = transactions.flatMap { tx ->
                    tx.splitInstallmentsByFirstPaymentDate(state.value.selectedCard)
                }
                // Her ayın işlemine bak bu ay olanları seç
                val thisMonthTransactions =
                    expandedTransactions.filter { it.firstPaymentDate.isInThisMonth() }
                // Bu aydan sonraki işlemler
                val totalFutureTransactions = expandedTransactions.filter {
                    it.firstPaymentDate.isAfterOrEqual(
                        getCurrentAppDate(day = state.value.selectedCard?.statementDay ?: 1)
                    )
                }

                val totalSpentThisMonth = thisMonthTransactions.map { it.amount }.sumWithoutCurrencyConverted()
                val totalFutureExpenses = totalFutureTransactions.map { it.amount }.sumWithoutCurrencyConverted()

                setState {
                    copy(
                        selectedCardCurrentTotalExpense = totalSpentThisMonth,
                        selectedCardFutureTotalExpense = totalFutureExpenses
                    )
                }

            }.launchIn(viewModelScope)

    }

}