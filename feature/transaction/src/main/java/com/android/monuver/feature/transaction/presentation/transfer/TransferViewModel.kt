package com.android.monuver.feature.transaction.presentation.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.android.monuver.feature.transaction.domain.model.TransferState
import com.android.monuver.feature.transaction.domain.usecase.CreateTransferTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class TransferViewModel(
    private val createTransferTransactionUseCase: CreateTransferTransactionUseCase,
    getActiveAccountsUseCase: GetActiveAccountsUseCase
) : ViewModel() {

    val accounts = getActiveAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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