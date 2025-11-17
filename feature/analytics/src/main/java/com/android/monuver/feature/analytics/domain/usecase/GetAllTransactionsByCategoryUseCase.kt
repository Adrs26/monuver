package com.android.monuver.feature.analytics.domain.usecase

import com.android.monuver.feature.analytics.domain.repository.AnalyticsRepository
import com.android.monuver.core.domain.model.TransactionState
import kotlinx.coroutines.flow.Flow

internal class GetAllTransactionsByCategoryUseCase(
    private val repository: AnalyticsRepository
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