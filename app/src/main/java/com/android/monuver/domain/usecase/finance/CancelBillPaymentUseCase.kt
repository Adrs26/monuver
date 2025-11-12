package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.repository.FinanceRepository

class CancelBillPaymentUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(billId: Long): DatabaseResultState {
        repository.cancelBillPayment(billId)
        return DatabaseResultState.CancelBillSuccess
    }
}