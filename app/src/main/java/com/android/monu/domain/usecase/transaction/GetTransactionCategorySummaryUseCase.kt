package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.transaction.TransactionCategorySummary
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionCategorySummaryUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(type: Int, month: Int, year: Int): Flow<List<TransactionCategorySummary>> {
        return repository.getGroupedMonthlyTransactionAmountByParentCategory(type, month, year)
    }
}