package com.android.monu.data.repository

import com.android.monu.data.local.dao.BillDao
import com.android.monu.data.mapper.BillMapper
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BillRepositoryImpl(
    private val billDao: BillDao
) : BillRepository {

    override fun getPendingBills(): Flow<List<Bill>> {
        return billDao.getPendingBills().map { bills ->
            bills.map { bill ->
                BillMapper.billEntityToDomain(bill)
            }
        }
    }

    override fun getDueBills(): Flow<List<Bill>> {
        return billDao.getDueBills().map { bills ->
            bills.map { bill ->
                BillMapper.billEntityToDomain(bill)
            }
        }
    }

    override suspend fun createNewBill(bill: Bill) {
        billDao.createNewBill(BillMapper.billDomainToEntity(bill))
    }
}