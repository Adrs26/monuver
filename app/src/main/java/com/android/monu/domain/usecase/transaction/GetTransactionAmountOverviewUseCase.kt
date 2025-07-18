package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.transaction.TransactionMonthlyAmountOverview
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionAmountOverviewUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<TransactionMonthlyAmountOverview> {
        return repository.getTransactionMonthlyAmountOverview(month, year)
    }
}