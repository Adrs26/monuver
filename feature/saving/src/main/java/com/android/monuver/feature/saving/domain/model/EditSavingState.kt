package com.android.monuver.feature.saving.domain.model

internal data class EditSavingState(
    val id: Long,
    val title: String,
    val targetDate: String,
    val currentAmount: Long,
    val targetAmount: Long
)