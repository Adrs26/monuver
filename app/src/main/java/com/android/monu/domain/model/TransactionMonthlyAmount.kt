package com.android.monu.domain.model

data class TransactionMonthlyAmount(
    val month: Int,
    val totalAmountIncome: Long,
    val totalAmountExpense: Long
)
