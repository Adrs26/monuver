package com.android.monuver.ui.feature.screen.budgeting.inactiveBudget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.domain.usecase.budget.GetAllInactiveBudgetsUseCase

class InactiveBudgetViewModel(
    getAllInactiveBudgetsUseCase: GetAllInactiveBudgetsUseCase
) : ViewModel() {

    val budgets = getAllInactiveBudgetsUseCase(viewModelScope)
}