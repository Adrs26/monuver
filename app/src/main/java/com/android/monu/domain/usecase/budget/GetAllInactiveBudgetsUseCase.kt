package com.android.monu.domain.usecase.budget

import androidx.paging.PagingData
import com.android.monu.domain.model.BudgetState
import com.android.monu.domain.repository.BudgetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class GetAllInactiveBudgetsUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(scope: CoroutineScope): Flow<PagingData<BudgetState>> {
        return repository.getAllInactiveBudgets(scope)
    }
}