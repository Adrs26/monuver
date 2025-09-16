package com.android.monu.ui.feature.screen.account.editAccount

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.usecase.account.GetAccountByIdUseCase
import com.android.monu.domain.usecase.finance.UpdateAccountUseCase
import com.android.monu.ui.feature.screen.account.editAccount.components.EditAccountContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.navigation.AccountDetail
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
    private val _account = MutableStateFlow<Account?>(null)
    val account = _account
        .onStart {
            val id = savedStateHandle.toRoute<AccountDetail.Edit>().accountId
            getAccountById(id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val initialAccount = MutableStateFlow<Account?>(null)

    private val _updateResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val updateResult = _updateResult.asStateFlow()

    private fun getAccountById(id: Int) {
        viewModelScope.launch {
            getAccountByIdUseCase(id).collect { account ->
                initialAccount.value = account
                _account.value = account
            }
        }
    }

    fun changeAccountType(type: Int) {
        _account.update { account ->
            account?.copy(type = type)
        }
    }

    fun restoreOriginalAccount() {
        initialAccount.value.let {
            _account.value = it
        }
    }

    fun updateAccount(accountState: EditAccountContentState) {
        viewModelScope.launch {
            _updateResult.value = updateAccountUseCase(accountState)
            delay(500)
            _updateResult.value = null
        }
    }
}