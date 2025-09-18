package com.yusufteker.worthy.screen.installments.list.presentation

import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentMonthUIModel
import com.yusufteker.worthy.screen.installments.list.domain.repository.InstallmentListRepository

class InstallmentListViewModel(
    private val installmentListRepository: InstallmentListRepository
) : BaseViewModel<InstallmentListState>(InstallmentListState()) {


    private fun observeData() {
        launchWithLoading {
            installmentListRepository.getAllInstallments().collect { installments ->
                val grouped = installments.groupBy {
                    val date = it.transaction.firstPaymentDate.toAppDate()
                    AppDate(year = date.year, month = date.month)
                }

                val sortedKeys = grouped.keys
                    .sortedWith(compareByDescending<AppDate> { it.year }.thenByDescending { it.month })

                val monthModels = sortedKeys.map { monthKey ->
                    val monthInstallments = grouped[monthKey].orEmpty()
                    // todo refund olanları alma çünkü refund olanları merge ettim normal expenseler gözikmicek refund ise 1 tane refund gözükecek ama amountunu yok sayıyoruz
                    // kod copluk oldu ama yapacak birşey yok bunu okuyan kişi zamanında yardım etse iyiydi
                    val totalAmount = monthInstallments.sumOf { if (it.transaction.transactionType == TransactionType.REFUND) 0.0 else it.transaction.amount.amount }
                    val currency = monthInstallments.firstOrNull()?.transaction?.amount?.currency ?: Currency.TRY

                    InstallmentMonthUIModel(
                        date = monthKey,
                        installments = monthInstallments,
                        totalAmount = totalAmount,
                        currency = currency,
                        isExpanded = false // default kapalı
                    )
                }

                // Current month varsa expand et
                val currentMonthKey = AppDate(getCurrentYear(), getCurrentMonth())
                val updatedModels = monthModels.map {
                    if (it.date == currentMonthKey) {
                        it.copy(isExpanded = true)
                    } else it
                }

                setState {
                    copy(
                        isLoading = false,
                        monthGroups = updatedModels // state içine koyuyoruz
                    )
                }
            }
        }
    }


    fun onAction(action: InstallmentListAction) {
        when (action) {
            is InstallmentListAction.Init -> {
                observeData()
            }
            is InstallmentListAction.NavigateBack -> {
                navigateBack()
            }

            is InstallmentListAction.OnMonthClicked -> {
                toggleMonth(action.month)
            }
        }
    }

    fun toggleMonth(month: AppDate) {
        setState {
            copy(
                monthGroups = monthGroups.map {
                    if (it.date == month) it.copy(isExpanded = !it.isExpanded) else it
                }
            )
        }
    }


}
