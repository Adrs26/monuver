package com.android.monu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monu.data.local.dao.BillDao
import com.android.monu.data.mapper.BillMapper
import com.android.monu.data.mapper.TransactionMapper
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.repository.BillRepository
import kotlinx.coroutines.CoroutineScope
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

    override fun getPaidBills(scope: CoroutineScope): Flow<PagingData<Bill>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                billDao.getPaidBills()
            }
        ).flow
            .map { pagingData ->
                pagingData.map { entity ->
                    BillMapper.billEntityToDomain(entity)
                }
            }.cachedIn(scope)
    }

    override fun getBillById(id: Long): Flow<Bill?> {
        return billDao.getBillById(id).map { bill ->
            bill?.let { BillMapper.billEntityToDomain(it) }
        }
    }

    override suspend fun createNewBill(bill: Bill) {
        billDao.createNewBill(BillMapper.billDomainToEntity(bill))
    }
}