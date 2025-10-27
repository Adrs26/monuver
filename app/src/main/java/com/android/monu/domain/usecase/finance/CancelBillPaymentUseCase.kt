package com.android.monu.domain.usecase.finance

import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.repository.FinanceRepository

class CancelBillPaymentUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(billId: Long): DatabaseResultState {
        repository.cancelBillPayment(billId)
        return DatabaseResultState.CancelBillSuccess
    }
}