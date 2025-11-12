package com.android.monuver.ui.feature.screen.budgeting.budgetDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.domain.common.CoroutineDispatchers
import com.android.monuver.domain.usecase.budget.DeleteBudgetUseCase
import com.android.monuver.domain.usecase.budget.GetBudgetByIdUseCase
import com.android.monuver.domain.usecase.transaction.GetTransactionsByCategoryAndDateRangeUseCase
import com.android.monuver.ui.feature.components.TransactionListItemState
import com.android.monuver.ui.navigation.BudgetDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getBudgetByIdUseCase: GetBudgetByIdUseCase,
    getTransactionsByCategoryAndDateRangeUseCase: GetTransactionsByCategoryAndDateRangeUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    coroutineDispatchers: CoroutineDispatchers
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
        }.map { transactions ->
            transactions.map { transactionState ->
                TransactionListItemState(
                    id = transactionState.id,
                    title = transactionState.title,
                    type = transactionState.type,
                    parentCategory = transactionState.parentCategory,
                    childCategory = transactionState.childCategory,
                    date = transactionState.date,
                    amount = transactionState.amount,
                    sourceName = transactionState.sourceName,
                    isLocked = transactionState.isLocked
                )
            }
        }
        .flowOn(coroutineDispatchers.default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteBudget(budgetId: Long) {
        viewModelScope.launch {
            deleteBudgetUseCase(budgetId)
        }
    }
}