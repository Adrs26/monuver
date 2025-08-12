package com.android.monu.domain.usecase.budgeting

import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.repository.BudgetingRepository
import kotlinx.coroutines.flow.Flow

class GetBudgetingByIdUseCase(
    private val repository: BudgetingRepository
) {
    operator fun invoke(id: Long): Flow<Budgeting?> {
        return repository.getBudgetingById(id)
    }
}