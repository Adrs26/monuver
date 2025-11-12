package com.android.monuver.domain.usecase.bill

import com.android.monuver.domain.model.BillState
import com.android.monuver.domain.repository.BillRepository

class GetUnpaidBillsUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(): List<BillState> {
        return repository.getAllUnpaidBills()
    }
}