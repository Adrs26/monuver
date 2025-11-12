package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.repository.FinanceRepository

class UpdateAccountStatusUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(accountId: Int, isActive: Boolean): DatabaseResultState {
        repository.updateAccountStatus(accountId, isActive)
        return if (isActive) {
            DatabaseResultState.ActivateAccountSuccess
        } else {
            DatabaseResultState.DeactivateAccountSuccess
        }
    }
}