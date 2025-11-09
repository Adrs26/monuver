package com.android.monu.domain.repository

import androidx.paging.PagingData
import com.android.monu.domain.model.BillState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface BillRepository {

    fun getPendingBills(): Flow<List<BillState>>

    fun getDueBills(): Flow<List<BillState>>

    fun getPaidBills(scope: CoroutineScope): Flow<PagingData<BillState>>

    fun getBillById(id: Long): Flow<BillState?>

    suspend fun createNewBill(billState: BillState): Long

    suspend fun updateParentId(id: Long, parentId: Long)

    suspend fun deleteBillById(billId: Long)

    suspend fun updateBill(billState: BillState)

    suspend fun updateBillPeriodByParentId(period: Int?, fixPeriod: Int?, parentId: Long)

    suspend fun getAllBills(): List<BillState>

    suspend fun getAllUnpaidBills(): List<BillState>
}