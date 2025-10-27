package com.android.monu.ui.feature.screen.transaction.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.TransferState
import com.android.monu.domain.usecase.account.GetActiveAccountsUseCase
import com.android.monu.domain.usecase.finance.CreateTransferTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransferViewModel(
    getActiveAccountsUseCase: GetActiveAccountsUseCase,
    private val createTransferTransactionUseCase: CreateTransferTransactionUseCase
) : ViewModel() {

    val accounts = getActiveAccountsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sourceAccount = MutableStateFlow(Pair(0, ""))
    val sourceAccount = _sourceAccount.asStateFlow()

    private val _destinationAccount = MutableStateFlow(Pair(0, ""))
    val destinationAccount = _destinationAccount.asStateFlow()

    val selectedAccounts = combine(
        _sourceAccount, _destinationAccount
    ) { source, destination ->
        listOf(source.first, destination.first)
    }

    private val _createResult = MutableStateFlow<DatabaseResultState?>(null)
    val createResult = _createResult.asStateFlow()

    fun changeSourceAccount(sourceId: Int, sourceName: String) {
        _sourceAccount.value = Pair(sourceId, sourceName)
    }

    fun changeDestinationAccount(destinationId: Int, destinationName: String) {
        _destinationAccount.value = Pair(destinationId, destinationName)
    }

    fun createNewTransfer(transferState: TransferState) {
        viewModelScope.launch {
            _createResult.value = createTransferTransactionUseCase(transferState)
            delay(500)
            _createResult.value = null
        }
    }
}