package com.android.monu.ui.feature.screen.account.accountDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.account.GetAccountByIdUseCase
import com.android.monu.domain.usecase.finance.UpdateAccountStatusUseCase
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.navigation.Account
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateAccountStatusUseCase: UpdateAccountStatusUseCase
) : ViewModel() {

    val account = getAccountByIdUseCase(
        savedStateHandle.toRoute<Account.Detail>().accountId
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateAccountStatus(accountId: Int, isActive: Boolean) {
        viewModelScope.launch {
            _updateResult.value = updateAccountStatusUseCase(accountId, isActive)
            delay(500)
            _updateResult.value = null
        }
    }
}