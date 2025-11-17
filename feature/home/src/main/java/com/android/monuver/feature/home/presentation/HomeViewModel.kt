package com.android.monuver.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.core.domain.common.CustomDispatcher
import com.android.monuver.core.domain.mapper.toListItemState
import com.android.monuver.core.domain.usecase.GetBudgetSummaryUseCase
import com.android.monuver.core.domain.usecase.HandleExpiredBudgetUseCase
import com.android.monuver.feature.home.domain.usecase.GetActiveAccountBalanceUseCase
import com.android.monuver.feature.home.domain.usecase.GetRecentTransactionsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val handleExpiredBudgetUseCase: HandleExpiredBudgetUseCase,
    getActiveAccountBalanceUseCase: GetActiveAccountBalanceUseCase,
    getRecentTransactionsUseCase: GetRecentTransactionsUseCase,
    getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    customDispatcher: CustomDispatcher
) : ViewModel() {

    val totalAccountBalance = getActiveAccountBalanceUseCase().map { it ?: 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val recentTransactions = getRecentTransactionsUseCase()
        .map { transactions ->
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

    val budgetSummaryState = getBudgetSummaryUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private var isExpiredBudgetHandled = false

    fun handleExpiredBudget() {
        if (!isExpiredBudgetHandled) {
            viewModelScope.launch {
                handleExpiredBudgetUseCase()
                isExpiredBudgetHandled = true
            }
        }
    }
}