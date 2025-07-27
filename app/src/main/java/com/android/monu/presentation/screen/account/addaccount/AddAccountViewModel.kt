package com.android.monu.presentation.screen.account.addaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.finance.CreateAccountUseCase
import com.android.monu.presentation.screen.account.addaccount.components.AddAccountContentState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddAccountViewModel(
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {

    private val _accountType = MutableStateFlow<Int>(0)
    val accountType = _accountType.asStateFlow()

    private val _createResult = MutableStateFlow<Result<Long>?>(null)
    val createResult = _createResult.asStateFlow()

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
                _createResult.value = Result.failure(
                    IllegalArgumentException("Semua field harus diisi ya")
                )
                delay(500)
                _createResult.value = null
            } else {
                _createResult.value = createAccountUseCase(accountState)
                _accountType.value = 0
            }
        }
    }
}