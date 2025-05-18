package com.android.monu.data.local.projection

data class TransactionConciseProj(
    val id: Long,
    val title: String,
    val type: Int,
    val category: Int,
    val date: String,
    val amount: Long
)