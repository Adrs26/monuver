package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.transaction.TransactionParentCategorySummary
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetGroupedMonthlyTransactionAmountByParentCategoryUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(type: Int, month: Int, year: Int): Flow<List<TransactionParentCategorySummary>> {
        return repository.getGroupedMonthlyTransactionAmountByParentCategory(type, month, year)
    }
}