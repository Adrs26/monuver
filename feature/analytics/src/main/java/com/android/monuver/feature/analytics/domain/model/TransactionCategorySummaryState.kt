package com.android.monuver.feature.analytics.domain.model

data class TransactionCategorySummaryState(
    val parentCategory: Int,
    val totalAmount: Long
)
