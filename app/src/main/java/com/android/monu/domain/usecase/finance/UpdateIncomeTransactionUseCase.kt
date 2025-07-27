package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.screen.transaction.edittransaction.components.EditTransactionContentState
import com.android.monu.presentation.utils.DateHelper

class UpdateIncomeTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(transactionState: EditTransactionContentState): Result<Int> {
        return try {
            val (month, year) = DateHelper.getMonthAndYear(transactionState.date)
            val transaction = Transaction(
                id = transactionState.id,
                title = transactionState.title,
                type = transactionState.type,
                parentCategory = transactionState.parentCategory,
                childCategory = transactionState.childCategory,
                date = transactionState.date,
                month = month,
                year = year,
                timeStamp = System.currentTimeMillis(),
                amount = transactionState.amount,
                sourceId = transactionState.sourceId,
                sourceName = transactionState.sourceName,
            )

            val rowUpdated = repository.updateIncomeTransaction(transaction, transactionState.startAmount)
            Result.success(rowUpdated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}