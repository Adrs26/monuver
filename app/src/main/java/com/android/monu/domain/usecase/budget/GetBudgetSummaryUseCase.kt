package com.android.monu.domain.usecase.budget

import com.android.monu.domain.model.BudgetSummaryState
import com.android.monu.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetBudgetSummaryUseCase(
    private val repository: BudgetRepository
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