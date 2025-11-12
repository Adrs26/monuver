package com.android.monuver.ui.feature.screen.account.addAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AddAccountState
import com.android.monuver.domain.usecase.finance.CreateAccountUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddAccountViewModel(
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {

    private val _accountType = MutableStateFlow(0)
    val accountType = _accountType.asStateFlow()

    private val _createResult = MutableStateFlow<DatabaseResultState?>(null)
    val createResult = _createResult.asStateFlow()

    fun changeAccountType(type: Int) {
        _accountType.value = type
    }

    fun createNewAccountWithInitialTransaction(accountState: AddAccountState) {
        viewModelScope.launch {
            _createResult.value = createAccountUseCase(accountState)
            delay(500)
            _createResult.value = null
        }
    }
}