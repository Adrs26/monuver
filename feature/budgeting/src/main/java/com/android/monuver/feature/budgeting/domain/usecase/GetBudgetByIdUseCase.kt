package com.android.monuver.feature.budgeting.domain.usecase

import com.android.monuver.feature.budgeting.domain.repository.BudgetRepository
import com.android.monuver.core.domain.model.BudgetState
import kotlinx.coroutines.flow.Flow

internal class GetBudgetByIdUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(id: Long): Flow<BudgetState?> {
        return repository.getBudgetById(id)
    }
}