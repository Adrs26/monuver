package com.android.monuver.feature.billing.domain.usecase

import com.android.monuver.feature.billing.domain.repository.BillRepository
import com.android.monuver.core.domain.model.BillState
import kotlinx.coroutines.flow.Flow

internal class GetDueBillsUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(): Flow<List<BillState>> {
        return repository.getDueBills()
    }
}