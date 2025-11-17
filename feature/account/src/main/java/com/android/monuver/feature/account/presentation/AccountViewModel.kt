package com.android.monuver.feature.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.feature.account.domain.usecase.GetAllAccountsUseCase
import com.android.monuver.feature.account.domain.usecase.GetTotalAccountBalanceUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class AccountViewModel(
    getTotalAccountBalanceUseCase: GetTotalAccountBalanceUseCase,
    getAllAccountsUseCase: GetAllAccountsUseCase
) : ViewModel() {

    val totalAccountBalance = getTotalAccountBalanceUseCase().map { it ?: 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val accounts = getAllAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}