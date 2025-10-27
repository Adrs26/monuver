package com.android.monu.domain.usecase.bill

import com.android.monu.domain.model.BillState
import com.android.monu.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow

class GetBillByIdUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(id: Long): Flow<BillState?> {
        return repository.getBillById(id)
    }
}