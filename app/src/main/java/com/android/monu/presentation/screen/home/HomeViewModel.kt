package com.android.monu.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.account.GetTotalAccountBalanceUseCase
import com.android.monu.domain.usecase.budgeting.GetBudgetingSummaryUseCase
import com.android.monu.domain.usecase.transaction.GetRecentTransactionsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    getTotalAccountBalanceUseCase: GetTotalAccountBalanceUseCase,
    getRecentTransactionsUseCase: GetRecentTransactionsUseCase,
    getBudgetingSummaryUseCase: GetBudgetingSummaryUseCase
) : ViewModel() {

    val totalAccountBalance = getTotalAccountBalanceUseCase().map{ it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recentTransactions = getRecentTransactionsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val budgetingSummary = getBudgetingSummaryUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}