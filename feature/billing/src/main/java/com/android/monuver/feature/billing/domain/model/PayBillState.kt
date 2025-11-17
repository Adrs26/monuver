package com.android.monuver.feature.billing.domain.model

internal data class PayBillState(
    val title: String,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String
)