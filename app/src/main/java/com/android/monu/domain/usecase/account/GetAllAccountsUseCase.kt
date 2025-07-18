package com.android.monu.domain.usecase.account

import com.android.monu.domain.repository.AccountRepository

class GetAllAccountsUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke() = repository.getAllAccounts()
}