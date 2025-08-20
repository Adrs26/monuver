package com.android.monu.ui.feature.screen.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.account.GetTotalAccountBalanceUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AccountViewModel(
    getTotalAccountBalanceUseCase: GetTotalAccountBalanceUseCase,
    getAllAccountsUseCase: GetAllAccountsUseCase
) : ViewModel() {

    val totalAccountBalance = getTotalAccountBalanceUseCase().map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val accounts = getAllAccountsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}