package com.android.monuver.ui.feature.screen.account.accountDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.usecase.account.GetAccountByIdUseCase
import com.android.monuver.domain.usecase.finance.UpdateAccountStatusUseCase
import com.android.monuver.ui.navigation.AccountDetail
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

    val accountState = getAccountByIdUseCase(
        savedStateHandle.toRoute<AccountDetail.Main>().accountId
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultState?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateAccountStatus(accountId: Int, isActive: Boolean) {
        viewModelScope.launch {
            _updateResult.value = updateAccountStatusUseCase(accountId, isActive)
            delay(500)
            _updateResult.value = null
        }
    }
}