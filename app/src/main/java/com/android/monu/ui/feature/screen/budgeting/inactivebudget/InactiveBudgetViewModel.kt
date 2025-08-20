package com.android.monu.ui.feature.screen.budgeting.inactivebudget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.budget.GetAllInactiveBudgetsUseCase

class InactiveBudgetViewModel(
    getAllInactiveBudgetsUseCase: GetAllInactiveBudgetsUseCase
) : ViewModel() {

    val budgets = getAllInactiveBudgetsUseCase(viewModelScope)
}