package com.android.monuver.domain.usecase.transaction

import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetRecentTransactionsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<TransactionState>> {
        return repository.getRecentTransactions()
    }
}