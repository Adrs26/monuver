package com.android.monuver.feature.budgeting.domain.usecase

import com.android.monuver.feature.budgeting.domain.repository.BudgetRepository
import com.android.monuver.core.domain.model.TransactionState
import kotlinx.coroutines.flow.Flow

internal class GetTransactionsByCategoryAndDateRangeUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<TransactionState>> {
        return repository.getTransactionsByParentCategoryAndDateRange(
            category = category,
            startDate = startDate,
            endDate = endDate
        )
    }
}