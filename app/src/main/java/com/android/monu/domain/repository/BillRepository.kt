package com.android.monu.domain.repository

import androidx.paging.PagingData
import com.android.monu.domain.model.bill.Bill
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface BillRepository {

    fun getPendingBills(): Flow<List<Bill>>

    fun getDueBills(): Flow<List<Bill>>

    fun getPaidBills(scope: CoroutineScope): Flow<PagingData<Bill>>

    fun getBillById(id: Long): Flow<Bill?>

    suspend fun createNewBill(bill: Bill)
}