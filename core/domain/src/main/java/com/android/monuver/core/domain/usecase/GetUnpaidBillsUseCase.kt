package com.android.monuver.core.domain.usecase

import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.repository.CoreRepository

class GetUnpaidBillsUseCase(
    private val repository: CoreRepository
) {
    suspend operator fun invoke(): List<BillState> {
        return repository.getAllUnpaidBills()
    }
}