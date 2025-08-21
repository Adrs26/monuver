package com.android.monu.domain.usecase.bill

import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow

class GetBillByIdUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(id: Long): Flow<Bill?> {
        return repository.getBillById(id)
    }
}