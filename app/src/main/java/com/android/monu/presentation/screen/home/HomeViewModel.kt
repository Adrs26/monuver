package com.android.monu.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.account.GetTotalAccountBalanceUseCase
import com.android.monu.domain.usecase.budget.GetBudgetSummaryUseCase
import com.android.monu.domain.usecase.budget.HandleExpiredBudgetUseCase
import com.android.monu.domain.usecase.transaction.GetRecentTransactionsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    getTotalAccountBalanceUseCase: GetTotalAccountBalanceUseCase,
    getRecentTransactionsUseCase: GetRecentTransactionsUseCase,
    getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    private val handleExpiredBudgetUseCase: HandleExpiredBudgetUseCase
) : ViewModel() {

    val totalAccountBalance = getTotalAccountBalanceUseCase().map{ it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recentTransactions = getRecentTransactionsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val budgetSummary = getBudgetSummaryUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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