package com.android.monuver.feature.transaction.domain.model

internal data class AddTransactionState(
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String
)
