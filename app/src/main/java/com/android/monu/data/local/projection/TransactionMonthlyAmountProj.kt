package com.android.monu.data.local.projection

data class TransactionMonthlyAmountProj(
    val month: Int,
    val totalAmountIncome: Long,
    val totalAmountExpense: Long
)
