package com.android.monuver.feature.budgeting.domain.usecase

import com.android.monuver.feature.budgeting.domain.model.EditBudgetState
import com.android.monuver.feature.budgeting.domain.repository.BudgetRepository
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.BudgetState

internal class UpdateBudgetUseCase(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budgetState: EditBudgetState): DatabaseResultState {
        if (budgetState.maxAmount == 0L) {
            return DatabaseResultState.EmptyBudgetMaxAmount
        }

        val totalAmount = budgetRepository.getTotalTransactionAmountInDateRange(
            category = budgetState.category,
            startDate = budgetState.startDate,
            endDate = budgetState.endDate
        )

        if (budgetState.maxAmount < totalAmount && !budgetState.isOverflowAllowed) {
            return DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit
        }

        val budget = BudgetState(
            id = budgetState.id,
            category = budgetState.category,
            cycle = budgetState.cycle,
            startDate = budgetState.startDate,
            endDate = budgetState.endDate,
            maxAmount = budgetState.maxAmount,
            usedAmount = totalAmount,
            isActive = true,
            isOverflowAllowed = budgetState.isOverflowAllowed,
            isAutoUpdate = budgetState.isAutoUpdate
        )

        budgetRepository.updateBudget(budget)
        return DatabaseResultState.UpdateBudgetSuccess
    }
}