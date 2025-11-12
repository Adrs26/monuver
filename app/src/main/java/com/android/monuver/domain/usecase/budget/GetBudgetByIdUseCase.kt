package com.android.monuver.domain.usecase.budget

import com.android.monuver.domain.model.BudgetState
import com.android.monuver.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

class GetBudgetByIdUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(id: Long): Flow<BudgetState?> {
        return repository.getBudgetById(id)
    }
}