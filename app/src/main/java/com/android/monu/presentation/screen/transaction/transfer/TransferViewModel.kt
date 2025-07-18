package com.android.monu.presentation.screen.transaction.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.finance.CreateTransactionAndAdjustAccountBalanceUseCase
import com.android.monu.presentation.screen.transaction.transfer.components.TransferContentState
import com.android.monu.utils.DataMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransferViewModel(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val createTransactionAndAdjustAccountBalanceUseCase: CreateTransactionAndAdjustAccountBalanceUseCase
) : ViewModel() {

    private val _sourceAccount = MutableStateFlow<Pair<Int, String>>(Pair(0, ""))
    val sourceAccount = _sourceAccount.asStateFlow()

    private val _destinationAccount = MutableStateFlow<Pair<Int, String>>(Pair(0, ""))
    val destinationAccount = _destinationAccount.asStateFlow()

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts
        .onStart {
            getAllAccounts()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedAccounts = combine(
        _sourceAccount, _destinationAccount
    ) { source, destination ->
        listOf(source.first, destination.first)
    }

    private val _createTransferResult = MutableStateFlow<Result<Long>?>(null)
    val createTransferResult = _createTransferResult.asStateFlow()

    fun changeSourceAccount(sourceId: Int, sourceName: String) {
        _sourceAccount.value = Pair(sourceId, sourceName)
    }

    fun changeDestinationAccount(destinationId: Int, destinationName: String) {
        _destinationAccount.value = Pair(destinationId, destinationName)
    }

    private fun getAllAccounts() {
        viewModelScope.launch {
            getAllAccountsUseCase().collect { accounts ->
                _accounts.value = accounts
            }
        }
    }

    fun createNewTransfer(transferState: TransferContentState) {
        viewModelScope.launch {
            if (
                transferState.sourceId == 0 ||
                transferState.destinationId == 0 ||
                transferState.date.isEmpty() ||
                transferState.amount == 0L
            ) {
                _createTransferResult.value = Result.failure(IllegalArgumentException())
                delay(500)
                _createTransferResult.value = null
            } else {
                val transaction = DataMapper.transferContentStateToTransaction(transferState)
                _createTransferResult.value = createTransactionAndAdjustAccountBalanceUseCase(transaction)
            }
        }
    }
}