package com.android.monuver.feature.saving.presentation.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.android.monuver.feature.saving.domain.model.DepositWithdrawState
import com.android.monuver.feature.saving.domain.usecase.CreateDepositTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class DepositViewModel(
    private val createDepositTransactionUseCase: CreateDepositTransactionUseCase,
    getActiveAccountsUseCase: GetActiveAccountsUseCase
) : ViewModel() {

    val accounts = getActiveAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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