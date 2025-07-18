package com.android.monu.data.local.projection

data class TransactionConciseProj(
    val id: Long,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceAccountId: Long? = null,
    val sourceAccountTitle: String? = null,
    val destinationAccountId: Long? = null,
    val destinationAccountTitle: String? = null,
    val sourceGoalId: Long? = null,
    val sourceGoalTitle: String? = null,
    val destinationGoalId: Long? = null,
    val destinationGoalTitle: String? = null
)