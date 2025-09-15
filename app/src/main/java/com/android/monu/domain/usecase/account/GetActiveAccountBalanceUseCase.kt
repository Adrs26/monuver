package com.android.monu.domain.usecase.account

import com.android.monu.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetActiveAccountBalanceUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<Long?> {
        return repository.getActiveAccountBalance()
    }
}