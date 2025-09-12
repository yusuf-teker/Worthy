package com.yusufteker.worthy.screen.installments.list.presentation

import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.installments.list.domain.repository.InstallmentListRepository

class InstallmentListViewModel(
    private val installmentListRepository: InstallmentListRepository
) : BaseViewModel<InstallmentListState>(InstallmentListState()) {


    private fun observeData() {
        launchWithLoading {
            installmentListRepository.getAllInstallments().collect { installments ->
                setState {
                    copy(
                        isLoading = false,
                        installments = installments
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
        }
    }



}

