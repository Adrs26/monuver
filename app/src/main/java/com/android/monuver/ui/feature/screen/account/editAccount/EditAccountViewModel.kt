package com.android.monuver.ui.feature.screen.account.editAccount

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AccountState
import com.android.monuver.domain.model.EditAccountState
import com.android.monuver.domain.usecase.account.GetAccountByIdUseCase
import com.android.monuver.domain.usecase.finance.UpdateAccountUseCase
import com.android.monuver.ui.navigation.AccountDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAccountViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase
) : ViewModel() {
    private val _accountState = MutableStateFlow<AccountState?>(null)
    val accountState = _accountState
        .onStart {
            val id = savedStateHandle.toRoute<AccountDetail.Edit>().accountId
            getAccountById(id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val initialAccountState = MutableStateFlow<AccountState?>(null)

    private val _updateResult = MutableStateFlow<DatabaseResultState?>(null)
    val updateResult = _updateResult.asStateFlow()

    private fun getAccountById(id: Int) {
        viewModelScope.launch {
            getAccountByIdUseCase(id).collect { account ->
                initialAccountState.value = account
                _accountState.value = account
            }
        }
    }

    fun changeAccountType(type: Int) {
        _accountState.update { accountState ->
            accountState?.copy(type = type)
        }
    }

    fun restoreOriginalAccount() {
        initialAccountState.value.let {
            _accountState.value = it
        }
    }

    fun updateAccount(accountState: EditAccountState) {
        viewModelScope.launch {
            _updateResult.value = updateAccountUseCase(accountState)
            delay(500)
            _updateResult.value = null
        }
    }
}