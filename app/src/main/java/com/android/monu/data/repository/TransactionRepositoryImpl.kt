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
        return transactionDao.getRecentTransactions().map { entityList ->
            entityList.map { entity ->
                TransactionMapper.transactionEntityToDomain(entity)
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
                pagingData.map { entity ->
                    TransactionMapper.transactionEntityToDomain(entity)
                }
            }
            .cachedIn(scope)
    }

    override fun getTransactionById(transactionId: Long): Flow<Transaction?> {
        return transactionDao.getTransactionById(transactionId).map { entity ->
            TransactionMapper.transactionEntityToDomain(entity ?: return@map null)
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
            .map { entityList ->
                entityList.map { entity ->
                    TransactionMapper.transactionCategorySummaryEntityToDomain(entity)
                }
            }
    }

    override fun getTransactionsInRange(
        startDate: String,
        endDate: String
    ): Flow<List<TransactionSummary>> {
        return transactionDao.getTransactionsInRange(startDate, endDate).map { entityList ->
            entityList.map { entity ->
                TransactionMapper.transactionSummaryEntityToDomain(entity)
            }
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Int> {
        return try {
            val result = transactionDao.updateTransaction(
                TransactionMapper.transactionDomainToEntityForUpdate(transaction)
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}