package com.android.monu.domain.usecase.budgeting

import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.repository.BudgetingRepository
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.presentation.screen.budgeting.addbudgeting.components.AddBudgetingContentState

class CreateBudgetingUseCase(
    private val budgetingRepository: BudgetingRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(budgetingState: AddBudgetingContentState): Result<Long> {
        return runCatching {
            if (budgetingRepository.isBudgetingExists(budgetingState.category)) {
                throw IllegalArgumentException("Budgeting aktif dengan kategori ini sudah ada")
            }

            val totalAmount = transactionRepository.getTotalTransactionAmountInDateRange(
                category = budgetingState.category,
                startDate = budgetingState.startDate,
                endDate = budgetingState.endDate
            )

            if (budgetingState.maxAmount < totalAmount && !budgetingState.isOverflowAllowed) {
                throw IllegalArgumentException("Jumlah budget saat ini melebihi batas maksimum")
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
        }
    }
}