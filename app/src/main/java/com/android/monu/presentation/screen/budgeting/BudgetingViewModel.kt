package com.android.monu.presentation.screen.budgeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.budget.BudgetSummary
import com.android.monu.domain.usecase.budget.GetAllActiveBudgetsUseCase
import com.android.monu.domain.usecase.budget.GetBudgetSummaryUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class BudgetingViewModel(
    getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    getAllActiveBudgetsUseCase: GetAllActiveBudgetsUseCase
) : ViewModel() {

    val budgetSummary = getBudgetSummaryUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetSummary(0, 0))

    val budgets = getAllActiveBudgetsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}