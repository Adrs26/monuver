package com.android.monu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.mapper.TransactionMapper
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.model.transaction.TransactionCategorySummary
import com.android.monu.domain.model.transaction.TransactionSummary
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getRecentTransactions(): Flow<List<Transaction>> {
        return transactionDao.getRecentTransactions().map { transactions ->
            transactions.map { transaction ->
                TransactionMapper.transactionEntityToDomain(transaction)
            }
        }
    }

    override fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<Transaction>> {
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
                pagingData.map { transaction ->
                    TransactionMapper.transactionEntityToDomain(transaction)
                }
            }.cachedIn(scope)
    }

    override fun getTransactionById(transactionId: Long): Flow<Transaction?> {
        return transactionDao.getTransactionById(transactionId).map { transaction ->
            transaction?.let { TransactionMapper.transactionEntityToDomain(it) }
        }
    }

    override fun getTransactionsBySavingId(savingId: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsBySavingId(savingId).map { transactions ->
            transactions.map { transaction ->
                TransactionMapper.transactionEntityToDomain(transaction)
            }
        }
    }

    override fun getTransactionsByParentCategoryAndDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByParentCategoryAndDateRange(
            category, startDate, endDate
        ).map { transactions ->
            transactions.map { transaction ->
                TransactionMapper.transactionEntityToDomain(transaction)
            }
        }
    }

    override fun getTransactionsByParentCategoryAndMonthAndYear(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByParentCategoryAndMonthAndYear(
            category, month, year
        ).map { transactions ->
            transactions.map { transaction ->
                TransactionMapper.transactionEntityToDomain(transaction)
            }
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
    ): Flow<List<TransactionCategorySummary>> {
        return transactionDao.getGroupedMonthlyTransactionAmountByParentCategory(type, month, year)
            .map { transactions ->
                transactions.map { transaction ->
                    TransactionMapper.transactionCategorySummaryEntityToDomain(transaction)
                }
            }
    }

    override fun getTransactionsInRange(
        startDate: String,
        endDate: String
    ): Flow<List<TransactionSummary>> {
        return transactionDao.getTransactionsInRange(startDate, endDate).map { transactions ->
            transactions.map { transaction ->
                TransactionMapper.transactionSummaryEntityToDomain(transaction)
            }
        }
    }

    override suspend fun getTotalTransactionAmountInDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Long {
        return transactionDao.getTotalTransactionAmountInDateRange(category, startDate, endDate)
    }

    override suspend fun getTransactionsBySavingIdSuspend(savingId: Long): List<Transaction> {
        return transactionDao.getTransactionsBySavingIdSuspend(savingId).map { transaction ->
            TransactionMapper.transactionEntityToDomain(transaction)
        }
    }

    override suspend fun getAllTransactionsSuspend(): List<Transaction> {
        return transactionDao.getAllTransactionsSuspend().map { transaction ->
            TransactionMapper.transactionEntityToDomain(transaction)
        }
    }

    override suspend fun insertAllTransactions(transactions: List<Transaction>) {
        return transactionDao.insertAllTransactions(transactions.map { transaction ->
            TransactionMapper.transactionDomainToEntity(transaction)
        })
    }
}