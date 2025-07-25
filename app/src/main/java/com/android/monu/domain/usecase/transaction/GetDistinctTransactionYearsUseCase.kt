package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetDistinctTransactionYearsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Int>> {
        return repository.getDistinctTransactionYears()
    }
}