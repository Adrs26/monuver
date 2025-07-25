package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionByIdUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(id: Long): Flow<Transaction?> {
        return repository.getTransactionById(id)
    }
}