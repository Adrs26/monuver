package com.android.monu.domain.usecase.finance

import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class UpdateAccountStatusUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(accountId: Int, isActive: Boolean): DatabaseResultMessage {
        repository.updateAccountStatus(accountId, isActive)
        return if (isActive) {
            DatabaseResultMessage.ActivateAccountSuccess
        } else {
            DatabaseResultMessage.DeactivateAccountSuccess
        }
    }
}