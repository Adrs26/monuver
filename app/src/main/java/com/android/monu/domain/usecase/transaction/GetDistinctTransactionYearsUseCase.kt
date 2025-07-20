package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.repository.TransactionRepository

class GetDistinctTransactionYearsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke() = repository.getDistinctTransactionYears()
}