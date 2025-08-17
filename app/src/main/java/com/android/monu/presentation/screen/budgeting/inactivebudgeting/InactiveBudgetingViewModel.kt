package com.android.monu.presentation.screen.budgeting.inactivebudgeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.budgeting.GetAllInactiveBudgetsUseCase

class InactiveBudgetingViewModel(
    getAllInactiveBudgetsUseCase: GetAllInactiveBudgetsUseCase
) : ViewModel() {

    val budgets = getAllInactiveBudgetsUseCase(viewModelScope)
}