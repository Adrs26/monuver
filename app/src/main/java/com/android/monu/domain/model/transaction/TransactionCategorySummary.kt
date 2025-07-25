package com.android.monu.domain.model.transaction

data class TransactionCategorySummary(
    val parentCategory: Int,
    val totalAmount: Long
)
