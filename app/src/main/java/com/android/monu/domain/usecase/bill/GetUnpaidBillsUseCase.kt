package com.android.monu.domain.usecase.bill

import com.android.monu.domain.model.BillState
import com.android.monu.domain.repository.BillRepository

class GetUnpaidBillsUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(): List<BillState> {
        return repository.getAllUnpaidBills()
    }
}