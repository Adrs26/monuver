package com.android.monuver.feature.billing.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.room.withTransaction
import com.android.monuver.core.data.database.MonuverDatabase
import com.android.monuver.core.data.database.dao.AccountDao
import com.android.monuver.core.data.database.dao.BillDao
import com.android.monuver.core.data.database.dao.BudgetDao
import com.android.monuver.core.data.database.dao.TransactionDao
import com.android.monuver.core.data.mapper.toDomain
import com.android.monuver.core.data.mapper.toEntity
import com.android.monuver.core.data.mapper.toEntityForUpdate
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.billing.domain.repository.BillRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BillRepositoryImpl(
    private val database: MonuverDatabase,
    private val accountDao: AccountDao,
    private val billDao: BillDao,
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
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

    override suspend fun updateBillParentId(id: Long, parentId: Long) {
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

    override suspend fun cancelBillPayment(billId: Long) {
        database.withTransaction {
            billDao.updateBillPaidStatusById(billId, null, false)
            val transaction = transactionDao.getTransactionIdByBillId(billId)
            transaction?.let {
                transactionDao.deleteTransactionById(transaction.id)
                accountDao.increaseAccountBalance(
                    accountId = transaction.sourceId,
                    delta = transaction.amount
                )
                budgetDao.decreaseBudgetUsedAmount(
                    category = transaction.parentCategory,
                    date = transaction.date,
                    delta = transaction.amount
                )
            }
        }
    }

    override suspend fun processBillPayment(
        billId: Long,
        billPaidDate: String,
        transactionState: TransactionState,
        isRecurring: Boolean,
        billState: BillState
    ) {
        database.withTransaction {
            billDao.updateBillPaidStatusById(
                billId = billId,
                paidDate = billPaidDate,
                isPaid = true
            )
            transactionDao.createNewTransaction(transactionState.toEntity())
            accountDao.decreaseAccountBalance(
                accountId = transactionState.sourceId,
                delta = transactionState.amount
            )
            budgetDao.increaseBudgetUsedAmount(
                category = transactionState.parentCategory,
                date = transactionState.date,
                delta = transactionState.amount
            )

            if (isRecurring) {
                billDao.createNewBill(billState.toEntity())
            }
        }
    }
}