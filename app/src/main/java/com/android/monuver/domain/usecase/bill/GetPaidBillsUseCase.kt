package com.android.monuver.domain.usecase.bill

import androidx.paging.PagingData
import com.android.monuver.domain.model.BillState
import com.android.monuver.domain.repository.BillRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class GetPaidBillsUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(scope: CoroutineScope): Flow<PagingData<BillState>> {
        return repository.getPaidBills(scope)
    }
}