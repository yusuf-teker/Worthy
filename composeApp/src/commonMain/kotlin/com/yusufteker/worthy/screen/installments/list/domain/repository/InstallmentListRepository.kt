package com.yusufteker.worthy.screen.installments.list.domain.repository

import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentCardUIModel
import kotlinx.coroutines.flow.Flow

interface InstallmentListRepository {
    fun getAllInstallments(): Flow<List<InstallmentCardUIModel>> // örnek method
    suspend fun addSomething(item: String)
}