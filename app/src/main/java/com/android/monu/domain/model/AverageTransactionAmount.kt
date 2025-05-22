package com.android.monu.domain.model

data class AverageTransactionAmount(
    val dailyAverageIncome: Double,
    val monthlyAverageIncome: Double,
    val yearlyAverageIncome: Double,
    val dailyAverageExpense: Double,
    val monthlyAverageExpense: Double,
    val yearlyAverageExpense: Double
)
