package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AccountState
import com.android.monuver.domain.model.EditAccountState
import com.android.monuver.domain.repository.FinanceRepository

class UpdateAccountUseCase(
    private val repository: FinanceRepository
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