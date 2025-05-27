package com.android.monu.domain.usecase

import com.android.monu.domain.repository.TransactionRepository

class GetTransactionMonthlyAmountsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(year: Int) = repository.getTransactionMonthlyAmounts(year)
}