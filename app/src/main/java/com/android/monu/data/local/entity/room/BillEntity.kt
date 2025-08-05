package com.android.monu.data.local.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bill")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val date: String,
    val isRecurringMonthly: Boolean,
    val amount: Long,
    val isPaid: Boolean,
    val isNotificationEnabled: Boolean
)
