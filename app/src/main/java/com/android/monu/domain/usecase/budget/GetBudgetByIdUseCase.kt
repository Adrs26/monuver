package com.android.monu.domain.usecase.budget

import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

class GetBudgetByIdUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(id: Long): Flow<Budget?> {
        return repository.getBudgetById(id)
    }
}