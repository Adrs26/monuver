package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository

class DeleteExpenseTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Int> {
        return runCatching {
            repository.deleteExpenseTransaction(
                id = transaction.id,
                parentCategory = transaction.parentCategory,
                date = transaction.date,
                sourceId = transaction.sourceId,
                amount = transaction.amount
            )
        }
    }
}
