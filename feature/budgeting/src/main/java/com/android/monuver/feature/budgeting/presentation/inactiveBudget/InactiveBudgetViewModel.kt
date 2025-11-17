package com.android.monuver.feature.budgeting.presentation.inactiveBudget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.android.monuver.core.domain.common.CustomDispatcher
import com.android.monuver.feature.budgeting.domain.model.BudgetListItemState
import com.android.monuver.feature.budgeting.domain.usecase.GetAllInactiveBudgetsUseCase
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class InactiveBudgetViewModel(
    getAllInactiveBudgetsUseCase: GetAllInactiveBudgetsUseCase,
    customDispatcher: CustomDispatcher
) : ViewModel() {

    val budgets = getAllInactiveBudgetsUseCase(viewModelScope)
        .map { pagingData ->
            pagingData.map { budgetState ->
                BudgetListItemState(
                    id = budgetState.id,
                    category = budgetState.category,
                    startDate = budgetState.startDate,
                    endDate = budgetState.endDate,
                    maxAmount = budgetState.maxAmount,
                    usedAmount = budgetState.usedAmount
                )
            }
        }.flowOn(customDispatcher.default)
}