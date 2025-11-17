package com.android.monuver.feature.saving.domain.model

internal data class AddSavingState(
    val title: String,
    val targetDate: String,
    val targetAmount: Long
)