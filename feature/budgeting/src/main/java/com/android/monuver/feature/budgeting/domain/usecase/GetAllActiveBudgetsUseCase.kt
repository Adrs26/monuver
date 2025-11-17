package com.android.monuver.feature.budgeting.domain.usecase

import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow

class GetAllActiveBudgetsUseCase(
    private val repository: CoreRepository
) {
    operator fun invoke(): Flow<List<BudgetState>> {
        return repository.getAllActiveBudgets()
    }
}