package com.android.monuver.domain.usecase.transaction

import com.android.monuver.domain.model.TransactionCategorySummaryState
import com.android.monuver.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionCategorySummaryUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(type: Int, month: Int, year: Int): Flow<List<TransactionCategorySummaryState>> {
        return repository.getGroupedMonthlyTransactionAmountByParentCategory(type, month, year)
    }
}