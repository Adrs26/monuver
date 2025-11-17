package com.android.monuver.feature.account.domain.usecase

import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.core.domain.common.DatabaseResultState

internal class UpdateAccountStatusUseCase(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(accountId: Int, isActive: Boolean): DatabaseResultState {
        repository.updateAccountStatus(
            accountId = accountId,
            isActive = isActive
        )
        return if (isActive) {
            DatabaseResultState.ActivateAccountSuccess
        } else {
            DatabaseResultState.DeactivateAccountSuccess
        }
    }
}