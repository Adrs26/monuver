package com.android.monuver.feature.billing.domain.model

internal data class BillListItemState(
    val id: Long,
    val title: String,
    val dueDate: String,
    val paidDate: String,
    val amount: Long,
    val isRecurring: Boolean,
    val status: Int,
    val nowPaidPeriod: Int,
)