package com.android.monu.ui.feature.screen.budgeting.budgetDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.budget.DeleteBudgetUseCase
import com.android.monu.domain.usecase.budget.GetBudgetByIdUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionsByCategoryAndDateRangeUseCase
import com.android.monu.ui.navigation.BudgetDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getBudgetByIdUseCase: GetBudgetByIdUseCase,
    getTransactionsByCategoryAndDateRangeUseCase: GetTransactionsByCategoryAndDateRangeUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase
) : ViewModel() {

    val budgetState = getBudgetByIdUseCase(
        savedStateHandle.toRoute<BudgetDetail.Main>().budgetId
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions = budgetState
        .filterNotNull()
        .flatMapLatest { budget ->
            getTransactionsByCategoryAndDateRangeUseCase(
                budget.category, budget.startDate, budget.endDate
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteBudget(budgetId: Long) {
        viewModelScope.launch {
            deleteBudgetUseCase(budgetId)
        }
    }
}