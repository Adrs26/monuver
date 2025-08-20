package com.android.monu.domain.repository

import com.android.monu.domain.model.bill.Bill
import kotlinx.coroutines.flow.Flow

interface BillRepository {

    fun getPendingBills(): Flow<List<Bill>>

    fun getDueBills(): Flow<List<Bill>>

    suspend fun createNewBill(bill: Bill)
}