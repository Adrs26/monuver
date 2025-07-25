package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.transaction.TransactionAmountSummary
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.presentation.utils.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTransactionAmountSummaryUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<TransactionAmountSummary> {
        val totalIncome = repository.getTotalMonthlyTransactionAmount(
            TransactionType.INCOME, month, year
        )
        val totalExpense = repository.getTotalMonthlyTransactionAmount(
            TransactionType.EXPENSE, month, year
        )
        val averageIncome = repository.getAverageDailyTransactionAmountInMonth(
            TransactionType.INCOME, month, year
        )
        val averageExpense = repository.getAverageDailyTransactionAmountInMonth(
            TransactionType.EXPENSE, month, year
        )

        return combine(
            totalIncome, totalExpense, averageIncome, averageExpense
        ) { totalIncome, totalExpense, averageIncome, averageExpense ->
            TransactionAmountSummary(
                totalIncomeAmount = totalIncome ?: 0L,
                totalExpenseAmount = totalExpense ?: 0L,
                averageIncomeAmount = averageIncome ?: 0.0,
                averageExpenseAmount = averageExpense ?: 0.0
            )
        }
    }
}