package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetAllTransactionsBySavingIdUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(savingId: Long): Flow<List<TransactionState>> {
        return repository.getTransactionsBySavingId(savingId)
    }
}