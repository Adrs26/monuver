package com.android.monuver.feature.transaction.domain.usecase

import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository

internal class DeleteIncomeTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transactionState: TransactionState) {
        repository.deleteIncomeTransaction(
            transactionId = transactionState.id,
            sourceId = transactionState.sourceId,
            amount = transactionState.amount
        )
    }
}