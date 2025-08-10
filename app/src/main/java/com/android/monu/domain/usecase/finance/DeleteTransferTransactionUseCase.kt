package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.utils.DatabaseResultMessage

class DeleteTransferTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(transaction: Transaction): DatabaseResultMessage {
        repository.deleteTransferTransaction(
            id = transaction.id,
            sourceId = transaction.sourceId,
            destinationId = transaction.destinationId ?: 0,
            amount = transaction.amount
        )
        return DatabaseResultMessage.DeleteTransactionSuccess
    }
}