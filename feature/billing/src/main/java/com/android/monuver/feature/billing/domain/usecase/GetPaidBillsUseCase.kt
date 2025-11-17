package com.android.monuver.feature.billing.domain.usecase

import androidx.paging.PagingData
import com.android.monuver.feature.billing.domain.repository.BillRepository
import com.android.monuver.core.domain.model.BillState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

internal class GetPaidBillsUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(scope: CoroutineScope): Flow<PagingData<BillState>> {
        return repository.getPaidBills(scope)
    }
}