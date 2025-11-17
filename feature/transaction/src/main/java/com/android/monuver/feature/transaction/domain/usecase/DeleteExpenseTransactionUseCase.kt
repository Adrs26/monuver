package com.android.monuver.feature.transaction.domain.usecase

import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository

internal class DeleteExpenseTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transactionState: TransactionState) {
        repository.deleteExpenseTransaction(
            transactionId = transactionState.id,
            parentCategory = transactionState.parentCategory,
            date = transactionState.date,
            sourceId = transactionState.sourceId,
            amount = transactionState.amount
        )
    }
}