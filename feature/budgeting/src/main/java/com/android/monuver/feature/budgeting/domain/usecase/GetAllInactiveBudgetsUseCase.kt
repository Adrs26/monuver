package com.android.monuver.feature.budgeting.domain.usecase

import androidx.paging.PagingData
import com.android.monuver.feature.budgeting.domain.repository.BudgetRepository
import com.android.monuver.core.domain.model.BudgetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

internal class GetAllInactiveBudgetsUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(scope: CoroutineScope): Flow<PagingData<BudgetState>> {
        return repository.getAllInactiveBudgets(scope)
    }
}