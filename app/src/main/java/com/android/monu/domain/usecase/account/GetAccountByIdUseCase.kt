package com.android.monu.domain.usecase.account

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetAccountByIdUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(accountId: Int): Flow<Account?> {
        return repository.getAccountById(accountId)
    }
}