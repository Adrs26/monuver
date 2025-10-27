package com.android.monu.domain.model

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

data class AddBudgetState(
    val category: Int,
    val maxAmount: Long,
    val cycle: Int,
    val startDate: String,
    val endDate: String,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)

data class EditBudgetState(
    val id: Long,
    val category: Int,
    val maxAmount: Long,
    val cycle: Int,
    val startDate: String,
    val endDate: String,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)

data class BudgetSummaryState(
    val totalMaxAmount: Long,
    val totalUsedAmount: Long
)