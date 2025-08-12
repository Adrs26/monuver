package com.android.monu.domain.usecase.budgeting

import com.android.monu.domain.repository.BudgetingRepository

class DeleteBudgetingUseCase(
    private val repository: BudgetingRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteBudgetingById(id)
    }
}