package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.FinanceRepository

class DeleteIncomeTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(transactionState: TransactionState) {
        repository.deleteIncomeTransaction(
            transactionId = transactionState.id,
            sourceId = transactionState.sourceId,
            amount = transactionState.amount
        )
    }
}