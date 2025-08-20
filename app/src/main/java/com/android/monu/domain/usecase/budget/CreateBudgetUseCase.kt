package com.android.monu.domain.usecase.budget

import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.ui.feature.screen.budgeting.addbudget.components.AddBudgetContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class CreateBudgetUseCase(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(budgetState: AddBudgetContentState): DatabaseResultMessage {
        when {
            budgetState.category == 0 -> return DatabaseResultMessage.EmptyBudgetCategory
            budgetState.maxAmount == 0L -> return DatabaseResultMessage.EmptyBudgetMaxAmount
        }

        if (budgetRepository.isBudgetExists(budgetState.category)) {
            return DatabaseResultMessage.ActiveBudgetWithCategoryAlreadyExists
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

        budgetRepository.createNewBudget(budget)
        return DatabaseResultMessage.CreateBudgetSuccess
    }
}