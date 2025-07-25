package com.android.monu.data.local.entity.projection

data class TransactionSummaryEntity(
    val type: Int,
    val date: String,
    val amount: Long
)