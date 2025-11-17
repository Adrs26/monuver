package com.android.monuver.feature.budgeting.domain.model

internal data class AddBudgetState(
    val category: Int,
    val maxAmount: Long,
    val cycle: Int,
    val startDate: String,
    val endDate: String,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)