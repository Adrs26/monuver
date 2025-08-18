package com.android.monu.domain.usecase.budget

import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

class GetAllActiveBudgetsUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(): Flow<List<Budget>> {
        return repository.getAllActiveBudgets()
    }
}