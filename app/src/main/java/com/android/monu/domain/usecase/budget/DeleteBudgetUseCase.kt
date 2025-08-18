package com.android.monu.domain.usecase.budget

import com.android.monu.domain.repository.BudgetRepository

class DeleteBudgetUseCase(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteBudgetById(id)
    }
}