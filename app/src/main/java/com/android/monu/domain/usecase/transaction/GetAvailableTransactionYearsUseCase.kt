package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.repository.TransactionRepository

class GetAvailableTransactionYearsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke() = repository.getAvailableTransactionYears()
}