package com.android.monu.domain.usecase.finance

import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.repository.FinanceRepository

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