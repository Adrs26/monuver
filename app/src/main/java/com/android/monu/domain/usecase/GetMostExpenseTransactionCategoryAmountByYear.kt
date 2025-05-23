package com.android.monu.domain.usecase

import com.android.monu.domain.repository.TransactionRepository

class GetMostExpenseTransactionCategoryAmountByYear(
    private val repository: TransactionRepository
) {
    operator fun invoke(year: Int) = repository.getMostExpenseTransactionCategoryAmountByYear(year)
}