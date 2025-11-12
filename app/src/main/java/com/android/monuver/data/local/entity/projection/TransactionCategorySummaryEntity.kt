package com.android.monuver.data.local.entity.projection

import androidx.room.ColumnInfo

data class TransactionCategorySummaryEntity(
    @ColumnInfo(name = "parent_category") val parentCategory: Int,
    @ColumnInfo(name = "total_amount") val totalAmount: Long
)