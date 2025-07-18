package com.android.monu.presentation.screen.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.account.GetTotalAccountBalanceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(
    private val getTotalAccountBalanceUseCase: GetTotalAccountBalanceUseCase,
    private val getAllAccountsUseCase: GetAllAccountsUseCase
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts
        .onStart {
            getAllAccounts()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _totalAccountBalance = MutableStateFlow<Long>(0)
    val totalAccountBalance = _totalAccountBalance
        .onStart {
            getTotalAccountBalance()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private fun getAllAccounts() {
        viewModelScope.launch {
            getAllAccountsUseCase().collect { accounts ->
                _accounts.value = accounts
            }
        }
    }

    private fun getTotalAccountBalance() {
        viewModelScope.launch {
            getTotalAccountBalanceUseCase().collect { balance ->
                _totalAccountBalance.value = balance ?: 0
            }
        }
    }
}