package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository

class DeleteTransferTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.deleteTransferTransaction(
            id = transaction.id,
            sourceId = transaction.sourceId,
            destinationId = transaction.destinationId ?: 0,
            amount = transaction.amount
        )
    }
}