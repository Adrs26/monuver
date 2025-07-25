package com.android.monu.domain.model.transaction

data class TransactionMonthlyAmountSummary(
    val totalIncomeAmount: Long = 0,
    val totalExpenseAmount: Long = 0,
    val averageIncomeAmount: Double = 0.0,
    val averageExpenseAmount: Double = 0.0
)
