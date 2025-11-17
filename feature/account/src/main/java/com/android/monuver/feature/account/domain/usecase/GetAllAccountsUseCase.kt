package com.android.monuver.feature.account.domain.usecase

import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.core.domain.model.AccountState
import kotlinx.coroutines.flow.Flow

internal class GetAllAccountsUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<List<AccountState>> {
        return repository.getAllAccounts()
    }
}