package com.android.monuver.domain.usecase.transaction

import com.android.monuver.domain.model.TransactionBalanceSummaryState
import com.android.monuver.domain.repository.TransactionRepository
import com.android.monuver.utils.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTransactionBalanceSummaryUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<TransactionBalanceSummaryState> {
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
            TransactionBalanceSummaryState(
                totalIncomeAmount = totalIncome ?: 0L,
                totalExpenseAmount = totalExpense ?: 0L,
                averageIncomeAmount = averageIncome ?: 0.0,
                averageExpenseAmount = averageExpense ?: 0.0
            )
        }
    }
}