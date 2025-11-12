package com.android.monuver.domain.usecase.bill

import com.android.monuver.domain.model.BillState
import com.android.monuver.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow

class GetPendingBillsUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(): Flow<List<BillState>> {
        return repository.getPendingBills()
    }
}