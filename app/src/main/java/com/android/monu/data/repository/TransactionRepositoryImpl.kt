package com.android.monu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.mapper.TransactionMapper
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.model.transaction.TransactionMonthlyAmount
import com.android.monu.domain.model.transaction.TransactionOverview
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getTotalTransactionAmount(type: Int): Flow<Long?> {
        return transactionDao.getTotalTransactionAmount(type)
    }

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

    override fun getAvailableTransactionYears(): Flow<List<Int>> {
        return transactionDao.getAvailableTransactionYears()
    }

    override fun getTransactionMonthlyAmounts(year: Int): Flow<List<TransactionMonthlyAmount>> {
        return transactionDao.getTransactionMonthlyAmounts(year).map { entityList ->
            entityList.map { entity ->
                TransactionMapper.transactionMonthlyAmountEntityToDomain(entity)
            }
        }
    }

    override fun getAverageTransactionAmountPerDay(type: Int): Flow<Double> {
        return transactionDao.getAverageTransactionAmountPerDay(type).map { it ?: 0.0 }
    }

    override fun getAverageTransactionAmountPerMonth(type: Int): Flow<Double> {
        return transactionDao.getAverageTransactionAmountPerMonth(type).map { it ?: 0.0 }
    }

    override fun getAverageTransactionAmountPerYear(type: Int): Flow<Double> {
        return transactionDao.getAverageTransactionAmountPerYear(type).map { it ?: 0.0 }
    }

    override fun getMonthlyTransactionOverviewsByType(
        type: Int,
        year: Int
    ): Flow<List<TransactionOverview>> {
        return transactionDao.getMonthlyTransactionOverviewsByType(type, year).map { entityList ->
            entityList.map { entity ->
                TransactionMapper.transactionOverviewEntityToDomain(entity)
            }
        }
    }

    override suspend fun createNewTransaction(transaction: Transaction): Result<Long> {
        return try {
            val result = transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transaction)
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
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

    override suspend fun deleteTransactionById(transactionId: Long): Result<Int> {
        return try {
            val result = transactionDao.deleteTransactionById(transactionId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}