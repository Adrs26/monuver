package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.repository.FinanceRepository

class CreateAccountWithInitialTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(account: Account): Result<Long> {
        return repository.createAccountWithInitialTransaction(account)
    }
}