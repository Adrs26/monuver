package com.android.monuver.core.domain.usecase

import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow

class GetActiveAccountsUseCase(
    private val repository: CoreRepository
) {
    operator fun invoke(): Flow<List<AccountState>> {
        return repository.getActiveAccounts()
    }
}