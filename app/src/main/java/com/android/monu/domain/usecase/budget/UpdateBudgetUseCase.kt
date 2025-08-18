package com.android.monu.domain.usecase.budget

import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.presentation.screen.budgeting.editbudget.components.EditBudgetContentState
import com.android.monu.presentation.utils.DatabaseResultMessage

class UpdateBudgetUseCase(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(budgetState: EditBudgetContentState): DatabaseResultMessage {
        if (budgetState.maxAmount == 0L) {
            return DatabaseResultMessage.EmptyBudgetMaxAmount
        }

        val totalAmount = transactionRepository.getTotalTransactionAmountInDateRange(
            category = budgetState.category,
            startDate = budgetState.startDate,
            endDate = budgetState.endDate
        )

        if (budgetState.maxAmount < totalAmount && !budgetState.isOverflowAllowed) {
            return DatabaseResultMessage.CurrentBudgetAmountExceedsMaximumLimit
        }

        val budget = Budget(
            id = budgetState.id,
            category = budgetState.category,
            period = budgetState.period,
            startDate = budgetState.startDate,
            endDate = budgetState.endDate,
            maxAmount = budgetState.maxAmount,
            usedAmount = totalAmount,
            isActive = true,
            isOverflowAllowed = budgetState.isOverflowAllowed,
            isAutoUpdate = budgetState.isAutoUpdate
        )

        budgetRepository.updateBudget(budget)
        return DatabaseResultMessage.UpdateBudgetSuccess
    }
}