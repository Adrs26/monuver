package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsByCategoryAndDateRangeUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(category: Int, startDate: String, endDate: String): Flow<List<TransactionState>> {
        return repository.getTransactionsByParentCategoryAndDateRange(category, startDate, endDate)
    }
}