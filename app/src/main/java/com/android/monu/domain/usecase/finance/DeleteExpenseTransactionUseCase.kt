package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository

class DeleteExpenseTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Int> {
        return try {
            val rowDeleted = repository.deleteExpenseTransaction(
                id = transaction.id,
                sourceId = transaction.sourceId,
                amount = transaction.amount
            )
            Result.success(rowDeleted)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}