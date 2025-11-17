package com.android.monuver.feature.billing.domain.model

internal data class AddBillState(
    val title: String,
    val date: String,
    val amount: Long,
    val isRecurring: Boolean,
    val cycle: Int,
    val period: Int,
    val fixPeriod: String
)