package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsByCategoryAndDateRangeUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(category: Int, startDate: String, endDate: String): Flow<List<Transaction>> {
        return repository.getTransactionsByParentCategoryAndDateRange(category, startDate, endDate)
    }
}