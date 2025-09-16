package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.screen.account.editAccount.components.EditAccountContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class UpdateAccountUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(accountState: EditAccountContentState): DatabaseResultMessage {
        when {
            accountState.name.isEmpty() -> return DatabaseResultMessage.EmptyAccountName
            accountState.type == 0 -> return DatabaseResultMessage.EmptyAccountType
        }

        val account = Account(
            id = accountState.id,
            name = accountState.name,
            type = accountState.type,
            balance = accountState.balance,
            isActive = true
        )

        repository.updateAccount(account)
        return DatabaseResultMessage.UpdateAccountSuccess
    }
}