package com.android.monuver.feature.account.presentation.accountDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.feature.account.domain.usecase.GetAccountByIdUseCase
import com.android.monuver.feature.account.domain.usecase.UpdateAccountStatusUseCase
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.presentation.navigation.AccountDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class AccountDetailViewModel(
    private val updateAccountStatusUseCase: UpdateAccountStatusUseCase,
    savedStateHandle: SavedStateHandle,
    getAccountByIdUseCase: GetAccountByIdUseCase
) : ViewModel() {

    val accountState = getAccountByIdUseCase(
        savedStateHandle.toRoute<AccountDetail.Main>().accountId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _updateResult = MutableStateFlow<DatabaseResultState?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateAccountStatus(accountId: Int, isActive: Boolean) {
        viewModelScope.launch {
            _updateResult.value = updateAccountStatusUseCase(
                accountId = accountId,
                isActive = isActive
            )
            delay(500)
            _updateResult.value = null
        }
    }
}