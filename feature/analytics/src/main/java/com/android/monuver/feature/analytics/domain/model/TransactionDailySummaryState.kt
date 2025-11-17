package com.android.monuver.feature.analytics.domain.model

data class TransactionDailySummaryState(
    val date: String,
    val totalIncome: Long,
    val totalExpense: Long
)
