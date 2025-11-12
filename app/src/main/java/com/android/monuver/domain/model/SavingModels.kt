package com.android.monuver.domain.model

data class SavingState(
    val id: Long = 0L,
    val title: String,
    val targetDate: String,
    val targetAmount: Long,
    val currentAmount: Long,
    val isActive: Boolean
)

data class AddSavingState(
    val title: String,
    val targetDate: String,
    val targetAmount: Long
)

data class DepositWithdrawState(
    val date: String,
    val amount: Long,
    val accountId: Int,
    val accountName: String,
    val savingId: Long,
    val savingName: String
)

data class EditSavingState(
    val id: Long,
    val title: String,
    val targetDate: String,
    val currentAmount: Long,
    val targetAmount: Long
)