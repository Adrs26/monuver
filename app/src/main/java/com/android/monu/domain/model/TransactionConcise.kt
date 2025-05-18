package com.android.monu.domain.model

data class TransactionConcise(
    val id: Long,
    val title: String,
    val type: Int,
    val category: Int,
    val date: String,
    val amount: Long
)
