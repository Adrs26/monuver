package com.android.monu.domain.usecase.bill

import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow

class GetPendingBillsUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(): Flow<List<Bill>> {
        return repository.getPendingBills()
    }
}