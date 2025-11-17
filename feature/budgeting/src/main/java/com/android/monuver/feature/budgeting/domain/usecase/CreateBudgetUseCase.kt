package com.android.monuver.feature.budgeting.domain.usecase

import com.android.monuver.feature.budgeting.domain.model.AddBudgetState
import com.android.monuver.feature.budgeting.domain.repository.BudgetRepository
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.repository.CoreRepository

internal class CreateBudgetUseCase(
    private val coreRepository: CoreRepository,
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budgetState: AddBudgetState): DatabaseResultState {
        when {
            budgetState.category == 0 -> return DatabaseResultState.EmptyBudgetCategory
            budgetState.maxAmount == 0L -> return DatabaseResultState.EmptyBudgetMaxAmount
        }

        if (budgetRepository.isBudgetExists(budgetState.category)) {
            return DatabaseResultState.ActiveBudgetWithCategoryAlreadyExists
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

        coreRepository.createNewBudget(budget)
        return DatabaseResultState.CreateBudgetSuccess
    }
}