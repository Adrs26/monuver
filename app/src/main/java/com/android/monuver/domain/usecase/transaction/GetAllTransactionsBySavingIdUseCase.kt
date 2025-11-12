package com.android.monuver.domain.usecase.transaction

import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetAllTransactionsBySavingIdUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(savingId: Long): Flow<List<TransactionState>> {
        return repository.getTransactionsBySavingId(savingId)
    }
}