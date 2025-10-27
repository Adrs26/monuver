package com.android.monu.domain.usecase.bill

import com.android.monu.domain.model.BillState
import com.android.monu.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow

class GetPendingBillsUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(): Flow<List<BillState>> {
        return repository.getPendingBills()
    }
}