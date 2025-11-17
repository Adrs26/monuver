package com.android.monuver.feature.billing.domain.usecase

import com.android.monuver.feature.billing.domain.repository.BillRepository
import com.android.monuver.core.domain.model.BillState
import kotlinx.coroutines.flow.Flow

internal class GetBillByIdUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(id: Long): Flow<BillState?> {
        return repository.getBillById(id)
    }
}