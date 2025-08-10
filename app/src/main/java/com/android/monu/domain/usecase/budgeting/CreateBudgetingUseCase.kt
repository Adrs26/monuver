package com.android.monu.domain.usecase.budgeting

import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.repository.BudgetingRepository
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.presentation.screen.budgeting.addbudgeting.components.AddBudgetingContentState
import com.android.monu.presentation.utils.DatabaseResultMessage

class CreateBudgetingUseCase(
    private val budgetingRepository: BudgetingRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(budgetingState: AddBudgetingContentState): DatabaseResultMessage {
        when {
            budgetingState.category == 0 -> return DatabaseResultMessage.EmptyBudgetingCategory
            budgetingState.maxAmount == 0L -> return DatabaseResultMessage.EmptyBudgetingMaxAmount
        }

        if (budgetingRepository.isBudgetingExists(budgetingState.category)) {
            return DatabaseResultMessage.ActiveBudgetingWithCategoryAlreadyExists
        }

        val totalAmount = transactionRepository.getTotalTransactionAmountInDateRange(
            category = budgetingState.category,
            startDate = budgetingState.startDate,
            endDate = budgetingState.endDate
        )

        if (budgetingState.maxAmount < totalAmount && !budgetingState.isOverflowAllowed) {
            return DatabaseResultMessage.CurrentBudgetAmountExceedsMaximumLimit
        }

        val budgeting = Budgeting(
            category = budgetingState.category,
            startDate = budgetingState.startDate,
            endDate = budgetingState.endDate,
            maxAmount = budgetingState.maxAmount,
            usedAmount = totalAmount,
            isActive = true,
            isOverflowAllowed = budgetingState.isOverflowAllowed,
            isAutoUpdate = budgetingState.isAutoUpdate
        )

        budgetingRepository.createNewBudgeting(budgeting)
        return DatabaseResultMessage.CreateBudgetingSuccess
    }
}