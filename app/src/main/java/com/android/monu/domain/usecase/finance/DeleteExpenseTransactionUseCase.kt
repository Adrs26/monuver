package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.utils.DatabaseResultMessage

class DeleteExpenseTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(transaction: Transaction): DatabaseResultMessage {
        repository.deleteExpenseTransaction(
            id = transaction.id,
            parentCategory = transaction.parentCategory,
            date = transaction.date,
            sourceId = transaction.sourceId,
            amount = transaction.amount
        )
        return DatabaseResultMessage.DeleteTransactionSuccess
    }
}
