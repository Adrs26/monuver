package com.android.monu.domain.usecase.budgeting

import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.repository.BudgetingRepository
import kotlinx.coroutines.flow.Flow

class GetAllActiveBudgetsUseCase(
    private val repository: BudgetingRepository
) {
    operator fun invoke(): Flow<List<Budgeting>> {
        return repository.getAllActiveBudgets()
    }
}