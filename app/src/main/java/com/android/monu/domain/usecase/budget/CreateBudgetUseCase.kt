package com.android.monu.domain.usecase.budget

import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.AddBudgetState
import com.android.monu.domain.model.BudgetState
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.TransactionRepository

class CreateBudgetUseCase(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(budgetState: AddBudgetState): DatabaseResultState {
        when {
            budgetState.category == 0 -> return DatabaseResultState.EmptyBudgetCategory
            budgetState.maxAmount == 0L -> return DatabaseResultState.EmptyBudgetMaxAmount
        }

        if (budgetRepository.isBudgetExists(budgetState.category)) {
            return DatabaseResultState.ActiveBudgetWithCategoryAlreadyExists
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
        return DatabaseResultState.CreateBudgetSuccess
    }
}