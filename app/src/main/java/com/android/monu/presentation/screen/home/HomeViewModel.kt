package com.android.monu.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.TransactionConcise
import com.android.monu.domain.usecase.GetRecentTransactionsUseCase
import com.android.monu.domain.usecase.GetTotalTransactionAmountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getTotalTransactionAmountUseCase: GetTotalTransactionAmountUseCase,
    private val getRecentTransactionsUseCase: GetRecentTransactionsUseCase
) : ViewModel() {

    private val _totalIncomeAmount = MutableStateFlow<Long>(0)
    val totalIncomeAmount = _totalIncomeAmount
        .onStart {
            getTotalTransactionAmount(1)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _totalExpenseAmount = MutableStateFlow<Long>(0)
    val totalExpenseAmount = _totalExpenseAmount
        .onStart {
            getTotalTransactionAmount(2)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _recentTransactions = MutableStateFlow<List<TransactionConcise>>(emptyList())
    val recentTransactions = _recentTransactions
        .onStart {
           viewModelScope.launch {
               getRecentTransactionsUseCase.invoke().collect { transactions ->
                   _recentTransactions.value = transactions
               }
           }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun getTotalTransactionAmount(type: Int) {
        viewModelScope.launch {
            getTotalTransactionAmountUseCase.invoke(type).collect { amount ->
                when (type) {
                    1 -> _totalIncomeAmount.value = amount ?: 0
                    2 -> _totalExpenseAmount.value = amount ?: 0
                }
            }
        }
    }
}