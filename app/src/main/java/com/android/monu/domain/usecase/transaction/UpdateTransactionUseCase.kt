package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.TransactionRepository

class UpdateTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) = repository.updateTransaction(transaction)
}