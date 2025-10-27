package com.android.monu.domain.usecase.budget

import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.BudgetState
import com.android.monu.domain.model.EditBudgetState
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.TransactionRepository

class UpdateBudgetUseCase(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(budgetState: EditBudgetState): DatabaseResultState {
        if (budgetState.maxAmount == 0L) {
            return DatabaseResultState.EmptyBudgetMaxAmount
        }

        val totalAmount = transactionRepository.getTotalTransactionAmountInDateRange(
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