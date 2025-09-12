package com.android.monu.ui.feature.screen.saving.withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.finance.CreateWithdrawTransactionUseCase
import com.android.monu.ui.feature.screen.saving.withdraw.components.WithdrawContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WithdrawViewModel(
    getAllAccountsUseCase: GetAllAccountsUseCase,
    private val createWithdrawTransactionUseCase: CreateWithdrawTransactionUseCase
) : ViewModel() {

    val accounts = getAllAccountsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _transactionDestination = MutableStateFlow(Pair(0, ""))
    val transactionDestination = _transactionDestination.asStateFlow()

    private val _createResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val createResult = _createResult.asStateFlow()

    fun changeTransactionDestination(destinationId: Int, destinationName: String) {
        _transactionDestination.value = Pair(destinationId, destinationName)
    }

    fun createNewTransaction(withdrawState: WithdrawContentState) {
        viewModelScope.launch {
            _createResult.value = createWithdrawTransactionUseCase(withdrawState)
            delay(500)
            _createResult.value = null
        }
    }
}