package com.android.monuver.domain.usecase.account

import com.android.monuver.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetTotalAccountBalanceUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<Long?> {
        return repository.getTotalAccountBalance()
    }
}