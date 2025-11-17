package com.android.monuver.feature.billing.domain.usecase

import com.android.monuver.feature.billing.domain.repository.BillRepository
import com.android.monuver.core.domain.common.DatabaseResultState

internal class CancelBillPaymentUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(billId: Long): DatabaseResultState {
        repository.cancelBillPayment(billId)
        return DatabaseResultState.CancelBillSuccess
    }
}