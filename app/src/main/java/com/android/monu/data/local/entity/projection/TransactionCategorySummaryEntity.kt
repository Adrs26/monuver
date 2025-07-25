package com.android.monu.data.local.entity.projection

data class TransactionCategorySummaryEntity(
    val parentCategory: Int,
    val totalAmount: Long
)