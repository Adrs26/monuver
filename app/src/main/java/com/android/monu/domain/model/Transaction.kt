package com.android.monu.domain.model

data class Transaction(
    val id: Long,
    val title: String,
    val type: Int,
    val category: Int,
    val date: String,
    val month: Int,
    val year: Int,
    val amount: Long,
    val budgetingId: Long? = null,
    val budgetingTitle: String? = null
)
