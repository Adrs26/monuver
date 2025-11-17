package com.android.monuver.feature.transaction.domain.usecase

import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

internal class GetTransactionByIdUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(id: Long): Flow<TransactionState?> {
        return repository.getTransactionById(id)
    }
}