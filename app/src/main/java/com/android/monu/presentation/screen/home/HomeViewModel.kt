package com.android.monu.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.account.GetTotalAccountBalanceUseCase
import com.android.monu.domain.usecase.transaction.GetRecentTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getTotalAccountBalanceUseCase: GetTotalAccountBalanceUseCase,
    private val getRecentTransactionsUseCase: GetRecentTransactionsUseCase
) : ViewModel() {

    private val _totalAccountBalance = MutableStateFlow<Long>(0)
    val totalAccountBalance = _totalAccountBalance
        .onStart {
            getTotalAccountBalance()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions = _recentTransactions
        .onStart {
            getRecentTransactions()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun getTotalAccountBalance() {
        viewModelScope.launch {
            getTotalAccountBalanceUseCase().collect { balance ->
                _totalAccountBalance.value = balance ?: 0
            }
        }
    }

    private fun getRecentTransactions() {
        viewModelScope.launch {
            getRecentTransactionsUseCase().collect { transactions ->
                _recentTransactions.value = transactions
            }
        }
    }
}