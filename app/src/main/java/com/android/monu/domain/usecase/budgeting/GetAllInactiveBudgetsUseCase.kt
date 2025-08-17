package com.android.monu.domain.usecase.budgeting

import androidx.paging.PagingData
import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.repository.BudgetingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class GetAllInactiveBudgetsUseCase(
    private val repository: BudgetingRepository
) {
    operator fun invoke(scope: CoroutineScope): Flow<PagingData<Budgeting>> {
        return repository.getAllInactiveBudgets(scope)
    }
}