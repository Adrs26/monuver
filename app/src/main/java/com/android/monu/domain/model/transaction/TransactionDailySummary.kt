package com.android.monu.domain.model.transaction

data class TransactionDailySummary(
    val date: String,
    val totalIncome: Long,
    val totalExpense: Long
)
