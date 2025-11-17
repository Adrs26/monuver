package com.android.monuver.core.domain.usecase

import com.android.monuver.core.domain.model.BudgetSummaryState
import com.android.monuver.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetBudgetSummaryUseCase(
    private val repository: CoreRepository
) {
    operator fun invoke(): Flow<BudgetSummaryState> {
        return combine(
            repository.getTotalBudgetMaxAmount(),
            repository.getTotalBudgetUsedAmount()
        ) { maxAmount, usedAmount ->
            BudgetSummaryState(
                totalMaxAmount = maxAmount,
                totalUsedAmount = usedAmount
            )
        }
    }
}