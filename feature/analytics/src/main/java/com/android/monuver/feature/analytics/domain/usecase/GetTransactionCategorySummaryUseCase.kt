package com.android.monuver.feature.analytics.domain.usecase

import com.android.monuver.feature.analytics.domain.model.TransactionCategorySummaryState
import com.android.monuver.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionCategorySummaryUseCase(
    private val repository: AnalyticsRepository
) {
    operator fun invoke(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummaryState>> {
        return repository.getGroupedMonthlyTransactionAmountByParentCategory(
            type = type,
            month = month,
            year = year
        )
    }
}