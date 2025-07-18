package com.android.monu.domain.usecase.account

import com.android.monu.domain.repository.AccountRepository

class GetTotalAccountBalanceUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke() = repository.getTotalAccountBalance()
}