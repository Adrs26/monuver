package com.android.monuver.feature.budgeting.presentation.budgetDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.core.domain.common.CustomDispatcher
import com.android.monuver.core.domain.mapper.toListItemState
import com.android.monuver.core.presentation.navigation.BudgetDetail
import com.android.monuver.feature.budgeting.domain.usecase.DeleteBudgetUseCase
import com.android.monuver.feature.budgeting.domain.usecase.GetBudgetByIdUseCase
import com.android.monuver.feature.budgeting.domain.usecase.GetTransactionsByCategoryAndDateRangeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class BudgetDetailViewModel(
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    savedStateHandle: SavedStateHandle,
    getBudgetByIdUseCase: GetBudgetByIdUseCase,
    getTransactionsByCategoryAndDateRangeUseCase: GetTransactionsByCategoryAndDateRangeUseCase,
    customDispatcher: CustomDispatcher
) : ViewModel() {

    val budgetState = getBudgetByIdUseCase(
        savedStateHandle.toRoute<BudgetDetail.Main>().budgetId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions = budgetState
        .filterNotNull()
        .flatMapLatest { budget ->
            getTransactionsByCategoryAndDateRangeUseCase(
                budget.category, budget.startDate, budget.endDate
            )
        }.map { transactions ->
            transactions.map { transactionState ->
                transactionState.toListItemState()
            }
        }
        .flowOn(customDispatcher.default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteBudget(budgetId: Long) {
        viewModelScope.launch {
            deleteBudgetUseCase(budgetId)
        }
    }
}