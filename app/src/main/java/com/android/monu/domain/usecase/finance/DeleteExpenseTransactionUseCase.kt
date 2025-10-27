package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.repository.FinanceRepository

class DeleteExpenseTransactionUseCase(
    private val repository: FinanceRepository
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
