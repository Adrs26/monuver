package com.android.monu.domain.model

data class Transaction(
    val id: Long,
    val title: String,
    val type: Int,
    val category: Int,
    val date: String,
    val month: Int,
    val year: Int,
    val timeStamp: Long,
    val amount: Long,
    val budgetingId: Long? = null,
    val budgetingTitle: String? = null,
    val billsId: Long? = null,
    val billsTitle: String? = null,
    val goalsId: Long? = null,
    val goalsTitle: String? = null
)
