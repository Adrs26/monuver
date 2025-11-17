package com.android.monuver.core.domain.model

data class SavingState(
    val id: Long = 0L,
    val title: String,
    val targetDate: String,
    val targetAmount: Long,
    val currentAmount: Long,
    val isActive: Boolean
)