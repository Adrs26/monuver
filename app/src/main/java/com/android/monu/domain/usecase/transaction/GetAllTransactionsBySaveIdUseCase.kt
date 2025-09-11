package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetAllTransactionsBySaveIdUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(saveId: Long): Flow<List<Transaction>> {
        return repository.getTransactionsBySaveId(saveId)
    }
}