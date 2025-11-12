package com.android.monuver.ui.feature.screen.budgeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.domain.common.CoroutineDispatchers
import com.android.monuver.domain.model.BudgetSummaryState
import com.android.monuver.domain.usecase.budget.GetAllActiveBudgetsUseCase
import com.android.monuver.domain.usecase.budget.GetBudgetSummaryUseCase
import com.android.monuver.domain.usecase.budget.HandleExpiredBudgetUseCase
import com.android.monuver.ui.feature.screen.budgeting.components.BudgetListItemState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetingViewModel(
    getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    getAllActiveBudgetsUseCase: GetAllActiveBudgetsUseCase,
    private val handleExpiredBudgetUseCase: HandleExpiredBudgetUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    val budgetSummaryState = getBudgetSummaryUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetSummaryState(0, 0))

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
        .flowOn(coroutineDispatchers.default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun handleExpiredBudget() {
        viewModelScope.launch {
            handleExpiredBudgetUseCase()
        }
    }
}