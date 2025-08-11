package com.android.monu.presentation.screen.budgeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.model.budgeting.BudgetingSummary
import com.android.monu.domain.usecase.budgeting.GetAllActiveBudgetsUseCase
import com.android.monu.domain.usecase.budgeting.GetBudgetingSummaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetingViewModel(
    private val getBudgetingSummaryUseCase: GetBudgetingSummaryUseCase,
    private val getAllActiveBudgetsUseCase: GetAllActiveBudgetsUseCase
) : ViewModel() {

    private val _budgetingSummary = MutableStateFlow<BudgetingSummary>(BudgetingSummary(0, 0))
    val budgetingSummary = _budgetingSummary
        .onStart {
            getBudgetingSummary()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetingSummary(0, 0))

    private val _budgets = MutableStateFlow<List<Budgeting>>(emptyList())
    val budgets = _budgets
        .onStart {
            getAllBudgets()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun getBudgetingSummary() {
        viewModelScope.launch {
            getBudgetingSummaryUseCase().collect { summary ->
                _budgetingSummary.value = summary
            }
        }
    }

    private fun getAllBudgets() {
        viewModelScope.launch {
            getAllActiveBudgetsUseCase().collect { budgets ->
                _budgets.value = budgets
            }
        }
    }
}