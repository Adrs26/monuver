package com.android.monu.domain.usecase.budget

import com.android.monu.domain.model.budget.BudgetSummary
import com.android.monu.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetBudgetSummaryUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(): Flow<BudgetSummary> {
        return combine(
            repository.getTotalBudgetMaxAmount(),
            repository.getTotalBudgetUsedAmount()
        ) { maxAmount, usedAmount ->
            BudgetSummary(
                totalMaxAmount = maxAmount,
                totalUsedAmount = usedAmount
            )
        }
    }
}