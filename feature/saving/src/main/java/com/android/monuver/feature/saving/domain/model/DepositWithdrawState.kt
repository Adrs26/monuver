package com.android.monuver.feature.saving.domain.model

internal data class DepositWithdrawState(
    val date: String,
    val amount: Long,
    val accountId: Int,
    val accountName: String,
    val savingId: Long,
    val savingName: String
)