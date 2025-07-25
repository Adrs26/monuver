package com.android.monu.domain.model.transaction

data class TransactionSummary(
    val type: Int,
    val date: String,
    val amount: Long
)
