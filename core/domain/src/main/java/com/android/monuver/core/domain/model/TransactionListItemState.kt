package com.android.monuver.core.domain.model

data class TransactionListItemState(
    val id: Long,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceName: String,
    val isLocked: Boolean
)