package com.android.monuver.feature.billing.domain.repository

import androidx.paging.PagingData
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.model.TransactionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

internal interface BillRepository {

    fun getPendingBills(): Flow<List<BillState>>

    fun getDueBills(): Flow<List<BillState>>

    fun getPaidBills(scope: CoroutineScope): Flow<PagingData<BillState>>

    fun getBillById(id: Long): Flow<BillState?>

    suspend fun createNewBill(billState: BillState): Long

    suspend fun updateBillParentId(id: Long, parentId: Long)

    suspend fun deleteBillById(billId: Long)

    suspend fun updateBill(billState: BillState)

    suspend fun updateBillPeriodByParentId(period: Int?, fixPeriod: Int?, parentId: Long)

    suspend fun cancelBillPayment(billId: Long)

    suspend fun processBillPayment(
        billId: Long,
        billPaidDate: String,
        transactionState: TransactionState,
        isRecurring: Boolean,
        billState: BillState
    )
}