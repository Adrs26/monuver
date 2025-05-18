package com.android.monu.domain.usecase

import com.android.monu.domain.model.Transaction
import com.android.monu.domain.repository.TransactionRepository

class InsertTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        return repository.insertTransaction(transaction)
    }
}