package com.android.monu.domain.model

data class BillState(
    val id: Long = 0L,
    val parentId: Long = 0L,
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
    val nowPaidPeriod: Int,
    val isPaidBefore: Boolean
)

data class AddBillState(
    val title: String,
    val date: String,
    val amount: Long,
    val isRecurring: Boolean,
    val cycle: Int,
    val period: Int,
    val fixPeriod: String
)

data class EditBillState(
    val id: Long,
    val parentId: Long,
    val title: String,
    val date: String,
    val amount: Long,
    val timeStamp: Long,
    val isRecurring: Boolean,
    val cycle: Int,
    val period: Int,
    val fixPeriod: String,
    val nowPaidPeriod: Int,
    val isPaidBefore: Boolean
)

data class PayBillState(
    val title: String,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String
)