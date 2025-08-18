package com.android.monu.data.local.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val category: Int,
    val period: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: Long,
    val usedAmount: Long,
    val isActive: Boolean,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)
