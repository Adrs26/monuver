package com.android.monuver.domain.usecase.account

import com.android.monuver.domain.model.AccountState
import com.android.monuver.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetAllAccountsUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<List<AccountState>> {
        return repository.getAllAccounts()
    }
}