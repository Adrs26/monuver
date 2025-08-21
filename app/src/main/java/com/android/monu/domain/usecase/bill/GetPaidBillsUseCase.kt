package com.android.monu.domain.usecase.bill

import androidx.paging.PagingData
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.repository.BillRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class GetPaidBillsUseCase(
    private val repository: BillRepository
) {
    operator fun invoke(scope: CoroutineScope): Flow<PagingData<Bill>> {
        return repository.getPaidBills(scope)
    }
}