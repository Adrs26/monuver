package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.transaction.TransactionDailySummary
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetWeeklyTransactionSummaryByDateRangeUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(month: Int, year: Int, week: Int): Flow<List<TransactionDailySummary>> {
        return repository.getWeeklyTransactionSummaryByDateRange(month, year, week)
    }
}