package com.android.monu.ui.feature.screen.budgeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.BudgetSummaryState
import com.android.monu.domain.usecase.budget.GetAllActiveBudgetsUseCase
import com.android.monu.domain.usecase.budget.GetBudgetSummaryUseCase
import com.android.monu.domain.usecase.budget.HandleExpiredBudgetUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetingViewModel(
    getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    getAllActiveBudgetsUseCase: GetAllActiveBudgetsUseCase,
    private val handleExpiredBudgetUseCase: HandleExpiredBudgetUseCase
) : ViewModel() {

    val budgetSummaryState = getBudgetSummaryUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetSummaryState(0, 0))

    val budgets = getAllActiveBudgetsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun handleExpiredBudget() {
        viewModelScope.launch {
            handleExpiredBudgetUseCase()
        }
    }
}