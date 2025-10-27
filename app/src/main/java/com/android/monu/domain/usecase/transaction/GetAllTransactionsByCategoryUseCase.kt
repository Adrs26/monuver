package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetAllTransactionsByCategoryUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionState>> {
        return repository.getTransactionsByParentCategoryAndMonthAndYear(
            category = category,
            month = month,
            year = year
        )
    }
}