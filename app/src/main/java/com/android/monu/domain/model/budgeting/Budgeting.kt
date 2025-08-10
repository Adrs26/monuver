package com.android.monu.domain.model.budgeting

data class Budgeting(
    val id: Long = 0L,
    val category: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: Long,
    val usedAmount: Long,
    val isActive: Boolean,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)
