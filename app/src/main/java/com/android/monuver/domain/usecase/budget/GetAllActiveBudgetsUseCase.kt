package com.android.monuver.domain.usecase.budget

import com.android.monuver.domain.model.BudgetState
import com.android.monuver.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

class GetAllActiveBudgetsUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(): Flow<List<BudgetState>> {
        return repository.getAllActiveBudgets()
    }
}