package com.android.monu.domain.model.transaction

data class TransactionMonthlyAmount(
    val month: Int,
    val totalAmountIncome: Long,
    val totalAmountExpense: Long
)
