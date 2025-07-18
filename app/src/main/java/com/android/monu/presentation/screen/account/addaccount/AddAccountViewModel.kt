package com.android.monu.presentation.screen.account.addaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.finance.CreateAccountWithInitialTransactionUseCase
import com.android.monu.presentation.screen.account.addaccount.components.AddAccountContentState
import com.android.monu.utils.DataMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddAccountViewModel(
    private val createAccountWithInitialTransactionUseCase: CreateAccountWithInitialTransactionUseCase
) : ViewModel() {

    private val _accountType = MutableStateFlow<Int>(0)
    val accountType = _accountType.asStateFlow()

    private val _createAccountResult = MutableStateFlow<Result<Long>?>(null)
    val createAccountResult = _createAccountResult.asStateFlow()

    fun changeAccountType(type: Int) {
        _accountType.value = type
    }

    fun createNewAccountWithInitialTransaction(accountState: AddAccountContentState) {
        viewModelScope.launch {
            if (
                accountState.name.isEmpty() ||
                accountState.type == 0 ||
                accountState.balance == 0L
                ) {
                _createAccountResult.value = Result.failure(IllegalArgumentException())
                delay(500)
                _createAccountResult.value = null
            } else {
                val account = DataMapper.accountContentStateToAccount(accountState)
                _createAccountResult.value = createAccountWithInitialTransactionUseCase(account)
                _accountType.value = 0
            }
        }
    }
}