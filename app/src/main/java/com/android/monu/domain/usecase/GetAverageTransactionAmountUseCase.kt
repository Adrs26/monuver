package com.android.monu.domain.usecase

import com.android.monu.domain.model.AverageTransactionAmount
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAverageTransactionAmountUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<AverageTransactionAmount> {
        val dailyAverageIncome = repository.getAverageTransactionAmountPerDay(type = 1)
        val monthlyAverageIncome = repository.getAverageTransactionAmountPerMonth(type = 1)
        val yearlyAverageIncome = repository.getAverageTransactionAmountPerYear(type = 1)

        val dailyAverageExpense = repository.getAverageTransactionAmountPerDay(type = 2)
        val monthlyAverageExpense = repository.getAverageTransactionAmountPerMonth(type = 2)
        val yearlyAverageExpense = repository.getAverageTransactionAmountPerYear(type = 2)

        return combine(
            dailyAverageIncome,
            monthlyAverageIncome,
            yearlyAverageIncome,
            dailyAverageExpense,
            monthlyAverageExpense,
            yearlyAverageExpense
        ) { values: Array<Double> ->
            AverageTransactionAmount(
                dailyAverageIncome = values[0],
                monthlyAverageIncome = values[1],
                yearlyAverageIncome = values[2],
                dailyAverageExpense = values[3],
                monthlyAverageExpense = values[4],
                yearlyAverageExpense = values[5],
            )
        }
    }
}