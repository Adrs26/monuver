package com.android.monuver.ui.feature.screen.saving.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.DepositWithdrawState
import com.android.monuver.domain.usecase.account.GetActiveAccountsUseCase
import com.android.monuver.domain.usecase.finance.CreateDepositTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositViewModel(
    getActiveAccountsUseCase: GetActiveAccountsUseCase,
    private val createDepositTransactionUseCase: CreateDepositTransactionUseCase
) : ViewModel() {

    val accounts = getActiveAccountsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _transactionSource = MutableStateFlow(Pair(0, ""))
    val transactionSource = _transactionSource.asStateFlow()

    private val _createResult = MutableStateFlow<DatabaseResultState?>(null)
    val createResult = _createResult.asStateFlow()

    fun changeTransactionSource(sourceId: Int, sourceName: String) {
        _transactionSource.value = Pair(sourceId, sourceName)
    }

    fun createNewTransaction(depositState: DepositWithdrawState) {
        viewModelScope.launch {
            _createResult.value = createDepositTransactionUseCase(depositState)
            delay(500)
            _createResult.value = null
        }
    }
}