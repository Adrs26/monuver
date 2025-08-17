package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository

class DeleteIncomeTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.deleteIncomeTransaction(
            id = transaction.id,
            sourceId = transaction.sourceId,
            amount = transaction.amount
        )
    }
}