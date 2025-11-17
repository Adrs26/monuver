package com.android.monuver.feature.analytics.domain.usecase

import com.android.monuver.feature.analytics.domain.model.TransactionBalanceSummaryState
import com.android.monuver.feature.analytics.domain.repository.AnalyticsRepository
import com.android.monuver.core.domain.util.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTransactionBalanceSummaryUseCase(
    private val repository: AnalyticsRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<TransactionBalanceSummaryState> {
        val totalIncome = repository.getTotalMonthlyTransactionAmount(
            type = TransactionType.INCOME,
            month = month,
            year = year
        )
        val totalExpense = repository.getTotalMonthlyTransactionAmount(
            type = TransactionType.EXPENSE,
            month = month,
            year = year
        )
        val averageIncome = repository.getAverageDailyTransactionAmountInMonth(
            type = TransactionType.INCOME,
            month = month,
            year = year
        )
        val averageExpense = repository.getAverageDailyTransactionAmountInMonth(
            type = TransactionType.EXPENSE,
            month = month,
            year = year
        )

        return combine(
            totalIncome,
            totalExpense,
            averageIncome,
            averageExpense
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