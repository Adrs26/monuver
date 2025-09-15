package com.android.monu.domain.usecase.finance

import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class CancelBillPaymentUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(billId: Long): DatabaseResultMessage {
        repository.cancelBillPayment(billId)
        return DatabaseResultMessage.CancelBillSuccess
    }
}