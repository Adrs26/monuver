package com.android.monuver.domain.usecase.bill

import com.android.monuver.domain.model.BillState
import com.android.monuver.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow

class GetBillByIdUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(id: Long): Flow<BillState?> {
        return repository.getBillById(id)
    }
}