package com.android.monu.data.local.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bill")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
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
