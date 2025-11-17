package com.android.monuver.feature.account.domain.usecase

import com.android.monuver.feature.account.domain.model.EditAccountState
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.AccountState

internal class UpdateAccountUseCase(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(accountState: EditAccountState): DatabaseResultState {
        when {
            accountState.name.isEmpty() -> return DatabaseResultState.EmptyAccountName
            accountState.type == 0 -> return DatabaseResultState.EmptyAccountType
        }

        val account = AccountState(
            id = accountState.id,
            name = accountState.name,
            type = accountState.type,
            balance = accountState.balance,
            isActive = true
        )

        repository.updateAccount(account)
        return DatabaseResultState.UpdateAccountSuccess
    }
}