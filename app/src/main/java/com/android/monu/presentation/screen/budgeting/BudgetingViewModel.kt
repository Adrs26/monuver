package com.android.monu.presentation.screen.budgeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.budgeting.BudgetingSummary
import com.android.monu.domain.usecase.budgeting.GetAllActiveBudgetsUseCase
import com.android.monu.domain.usecase.budgeting.GetBudgetingSummaryUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class BudgetingViewModel(
    getBudgetingSummaryUseCase: GetBudgetingSummaryUseCase,
    getAllActiveBudgetsUseCase: GetAllActiveBudgetsUseCase
) : ViewModel() {

    val budgetingSummary = getBudgetingSummaryUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetingSummary(0, 0))

    val budgets = getAllActiveBudgetsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}