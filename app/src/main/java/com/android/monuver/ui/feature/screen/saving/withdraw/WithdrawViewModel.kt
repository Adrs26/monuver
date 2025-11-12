package com.android.monuver.ui.feature.screen.saving.withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.DepositWithdrawState
import com.android.monuver.domain.usecase.account.GetActiveAccountsUseCase
import com.android.monuver.domain.usecase.finance.CreateWithdrawTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WithdrawViewModel(
    getActiveAccountsUseCase: GetActiveAccountsUseCase,
    private val createWithdrawTransactionUseCase: CreateWithdrawTransactionUseCase
) : ViewModel() {

    val accounts = getActiveAccountsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _transactionDestination = MutableStateFlow(Pair(0, ""))
    val transactionDestination = _transactionDestination.asStateFlow()

    private val _createResult = MutableStateFlow<DatabaseResultState?>(null)
    val createResult = _createResult.asStateFlow()

    fun changeTransactionDestination(destinationId: Int, destinationName: String) {
        _transactionDestination.value = Pair(destinationId, destinationName)
    }

    fun createNewTransaction(withdrawState: DepositWithdrawState) {
        viewModelScope.launch {
            _createResult.value = createWithdrawTransactionUseCase(withdrawState)
            delay(500)
            _createResult.value = null
        }
    }
}