package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.repository.FinanceRepository

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