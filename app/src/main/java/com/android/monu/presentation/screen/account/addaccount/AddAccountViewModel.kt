package com.android.monu.presentation.screen.account.addaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.finance.CreateAccountUseCase
import com.android.monu.presentation.screen.account.addaccount.components.AddAccountContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddAccountViewModel(
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {

    private val _accountType = MutableStateFlow<Int>(0)
    val accountType = _accountType.asStateFlow()

    private val _createResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val createResult = _createResult.asStateFlow()

    fun changeAccountType(type: Int) {
        _accountType.value = type
    }

    fun createNewAccountWithInitialTransaction(accountState: AddAccountContentState) {
        viewModelScope.launch {
            _createResult.value = createAccountUseCase(accountState)
            delay(500)
            _createResult.value = null
        }
    }
}