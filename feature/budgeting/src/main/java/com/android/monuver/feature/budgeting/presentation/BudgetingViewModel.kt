package com.android.monuver.feature.budgeting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.core.domain.common.CustomDispatcher
import com.android.monuver.core.domain.model.BudgetSummaryState
import com.android.monuver.core.domain.usecase.GetBudgetSummaryUseCase
import com.android.monuver.core.domain.usecase.HandleExpiredBudgetUseCase
import com.android.monuver.feature.budgeting.domain.model.BudgetListItemState
import com.android.monuver.feature.budgeting.domain.usecase.GetAllActiveBudgetsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetingViewModel(
    private val handleExpiredBudgetUseCase: HandleExpiredBudgetUseCase,
    getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    getAllActiveBudgetsUseCase: GetAllActiveBudgetsUseCase,
    customDispatcher: CustomDispatcher
) : ViewModel() {

    val budgetSummaryState = getBudgetSummaryUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BudgetSummaryState(0, 0)
        )

    val budgets = getAllActiveBudgetsUseCase()
        .map { budgets ->
            budgets.map { budgetState ->
                BudgetListItemState(
                    id = budgetState.id,
                    category = budgetState.category,
                    startDate = budgetState.startDate,
                    endDate = budgetState.endDate,
                    maxAmount = budgetState.maxAmount,
                    usedAmount = budgetState.usedAmount
                )
            }
        }
        .flowOn(customDispatcher.default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun handleExpiredBudget() {
        viewModelScope.launch {
            handleExpiredBudgetUseCase()
        }
    }
}