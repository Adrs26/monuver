package com.android.monuver.core.domain.model

data class BudgetState(
    val id: Long = 0L,
    val category: Int,
    val cycle: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: Long,
    val usedAmount: Long,
    val isActive: Boolean,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)