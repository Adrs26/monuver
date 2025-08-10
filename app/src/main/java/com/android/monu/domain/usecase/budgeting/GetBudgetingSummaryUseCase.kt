package com.android.monu.domain.usecase.budgeting

import com.android.monu.domain.model.budgeting.BudgetingSummary
import com.android.monu.domain.repository.BudgetingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetBudgetingSummaryUseCase(
    private val repository: BudgetingRepository
) {
    operator fun invoke(): Flow<BudgetingSummary> {
        return combine(
            repository.getTotalBudgetingMaxAmount(),
            repository.getTotalBudgetingUsedAmount()
        ) { maxAmount, usedAmount ->
            BudgetingSummary(
                totalMaxAmount = maxAmount,
                totalUsedAmount = usedAmount
            )
        }
    }
}