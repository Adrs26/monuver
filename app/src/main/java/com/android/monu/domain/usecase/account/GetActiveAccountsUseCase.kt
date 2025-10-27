package com.android.monu.domain.usecase.account

import com.android.monu.domain.model.AccountState
import com.android.monu.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetActiveAccountsUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<List<AccountState>> {
        return repository.getActiveAccounts()
    }
}