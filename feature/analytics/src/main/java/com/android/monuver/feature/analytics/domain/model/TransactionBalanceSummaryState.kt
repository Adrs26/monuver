package com.android.monuver.feature.analytics.domain.model

data class TransactionBalanceSummaryState(
    val totalIncomeAmount: Long = 0,
    val totalExpenseAmount: Long = 0,
    val averageIncomeAmount: Double = 0.0,
    val averageExpenseAmount: Double = 0.0
)
