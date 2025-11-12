package com.android.monuver.domain.usecase.account

import com.android.monuver.domain.model.AccountState
import com.android.monuver.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetAccountByIdUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(accountId: Int): Flow<AccountState?> {
        return repository.getAccountById(accountId)
    }
}