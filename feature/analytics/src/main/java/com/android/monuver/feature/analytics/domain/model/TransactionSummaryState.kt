package com.android.monuver.feature.analytics.domain.model

data class TransactionSummaryState(
    val type: Int,
    val date: String,
    val amount: Long
)
