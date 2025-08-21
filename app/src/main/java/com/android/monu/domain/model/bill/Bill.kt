package com.android.monu.domain.model.bill

data class Bill(
    val id: Long = 0L,
    val title: String,
    val dueDate: String,
    val paidDate: String?,
    val timeStamp: Long,
    val amount: Long,
    val isRecurring: Boolean,
    val cycle: Int?,
    val period: Int?,
    val fixPeriod: Int?,
    val isPaid: Boolean,
    val nowPaidPeriod: Int
)
