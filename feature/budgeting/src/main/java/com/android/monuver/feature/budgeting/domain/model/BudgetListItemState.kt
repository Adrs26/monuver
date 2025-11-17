package com.android.monuver.feature.budgeting.domain.model

data class BudgetListItemState(
    val id: Long,
    val category: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: Long,
    val usedAmount: Long,
)
