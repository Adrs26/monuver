package com.android.monu.data.local.entity.projection

import androidx.room.ColumnInfo

data class TransactionSummaryEntity(
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "amount") val amount: Long
)