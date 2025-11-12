package com.android.monuver.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monuver.data.local.dao.TransactionDao
import com.android.monuver.data.mapper.toDomain
import com.android.monuver.domain.model.TransactionCategorySummaryState
import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.model.TransactionSummaryState
import com.android.monuver.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getRecentTransactions(): Flow<List<TransactionState>> {
        return transactionDao.getRecentTransactions().map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<TransactionState>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                transactionDao.getAllTransactions(
                    query = query,
                    type = type,
                    month = month,
                    year = year
                )
            }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomain() }
            }.cachedIn(scope)
    }

    override fun getTransactionById(transactionId: Long): Flow<TransactionState?> {
        return transactionDao.getTransactionById(transactionId).map { transaction ->
            transaction?.toDomain()
        }
    }

    override fun getTransactionsBySavingId(savingId: Long): Flow<List<TransactionState>> {
        return transactionDao.getTransactionsBySavingId(savingId).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override fun getTransactionsByParentCategoryAndDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<TransactionState>> {
        return transactionDao.getTransactionsByParentCategoryAndDateRange(
            category, startDate, endDate
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override fun getTransactionsByParentCategoryAndMonthAndYear(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionState>> {
        return transactionDao.getTransactionsByParentCategoryAndMonthAndYear(
            category, month, year
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override fun getDistinctTransactionYears(): Flow<List<Int>> {
        return transactionDao.getDistinctTransactionYears()
    }

    override fun getTotalMonthlyTransactionAmount(type: Int, month: Int, year: Int): Flow<Long?> {
        return transactionDao.getTotalMonthlyTransactionAmount(type, month, year)
    }

    override fun getAverageDailyTransactionAmountInMonth(
        type: Int,
        month: Int,
        year: Int
    ): Flow<Double?> {
        return transactionDao.getAverageDailyTransactionAmountInMonth(type, month, year)
    }

    override fun getGroupedMonthlyTransactionAmountByParentCategory(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummaryState>> {
        return transactionDao.getGroupedMonthlyTransactionAmountByParentCategory(type, month, year)
            .map { transactions ->
                transactions.map { it.toDomain() }
            }
    }

    override fun getTransactionsInRange(
        startDate: String,
        endDate: String
    ): Flow<List<TransactionSummaryState>> {
        return transactionDao.getTransactionsInRange(startDate, endDate).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override suspend fun getTotalTransactionAmountInDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Long {
        return transactionDao.getTotalTransactionAmountInDateRange(category, startDate, endDate)
    }

    override suspend fun getTransactionsBySavingIdSuspend(savingId: Long): List<TransactionState> {
        return transactionDao.getTransactionsBySavingIdSuspend(savingId).map { it.toDomain() }
    }

    override suspend fun getAllTransactions(): List<TransactionState> {
        return transactionDao.getAllTransactions().map { it.toDomain() }
    }

    override suspend fun getTransactionsInRangeByDateAsc(
        startDate: String,
        endDate: String
    ): List<TransactionState> {
        return transactionDao.getTransactionsInRangeByDateAsc(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun getTransactionsInRangeByDateDesc(
        startDate: String,
        endDate: String
    ): List<TransactionState> {
        return transactionDao.getTransactionsInRangeByDateDesc(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun getTransactionsInRangeByDateAscWithType(
        startDate: String,
        endDate: String
    ): List<TransactionState> {
        return transactionDao.getTransactionsInRangeByDateAscWithType(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun getTransactionsInRangeByDateDescWithType(
        startDate: String,
        endDate: String
    ): List<TransactionState> {
        return transactionDao.getTransactionsInRangeByDateDescWithType(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun getTotalIncomeTransactionInRange(
        startDate: String,
        endDate: String
    ): Long? {
        return transactionDao.getTotalIncomeTransactionsInRange(startDate, endDate)
    }

    override suspend fun getTotalExpenseTransactionInRange(
        startDate: String,
        endDate: String
    ): Long? {
        return transactionDao.getTotalExpenseTransactionsInRange(startDate, endDate)
    }
}