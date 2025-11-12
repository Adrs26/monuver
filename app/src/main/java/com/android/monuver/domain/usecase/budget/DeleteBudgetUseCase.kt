package com.android.monuver.domain.usecase.budget

import com.android.monuver.domain.repository.BudgetRepository

class DeleteBudgetUseCase(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteBudgetById(id)
    }
}