package com.android.monuver.feature.saving.presentation.withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.android.monuver.feature.saving.domain.model.DepositWithdrawState
import com.android.monuver.feature.saving.domain.usecase.CreateWithdrawTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class WithdrawViewModel(
    private val createWithdrawTransactionUseCase: CreateWithdrawTransactionUseCase,
    getActiveAccountsUseCase: GetActiveAccountsUseCase
) : ViewModel() {

    val accounts = getActiveAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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