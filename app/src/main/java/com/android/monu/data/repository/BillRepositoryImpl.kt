package com.android.monu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monu.data.local.dao.BillDao
import com.android.monu.data.mapper.toDomain
import com.android.monu.data.mapper.toEntity
import com.android.monu.data.mapper.toEntityForUpdate
import com.android.monu.domain.model.BillState
import com.android.monu.domain.repository.BillRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BillRepositoryImpl(
    private val billDao: BillDao
) : BillRepository {

    override fun getPendingBills(): Flow<List<BillState>> {
        return billDao.getPendingBills().map { bills ->
            bills.map { it.toDomain() }
        }
    }

    override fun getDueBills(): Flow<List<BillState>> {
        return billDao.getDueBills().map { bills ->
            bills.map { it.toDomain() }
        }
    }

    override fun getPaidBills(scope: CoroutineScope): Flow<PagingData<BillState>> {
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
                pagingData.map { it.toDomain() }
            }.cachedIn(scope)
    }

    override fun getBillById(id: Long): Flow<BillState?> {
        return billDao.getBillById(id).map { it?.toDomain() }
    }

    override suspend fun createNewBill(billState: BillState): Long {
        return billDao.createNewBill(billState.toEntity())
    }

    override suspend fun updateParentId(id: Long, parentId: Long) {
        billDao.updateParentId(id, parentId)
    }

    override suspend fun deleteBillById(billId: Long) {
        billDao.deleteBillById(billId)
    }

    override suspend fun updateBill(billState: BillState) {
        billDao.updateBill(billState.toEntityForUpdate())
    }

    override suspend fun updateBillPeriodByParentId(
        period: Int?,
        fixPeriod: Int?,
        parentId: Long
    ) {
        billDao.updateBillPeriodByParentId(period, fixPeriod, parentId)
    }

    override suspend fun getAllBills(): List<BillState> {
        return billDao.getAllBills().map { it.toDomain() }
    }
}