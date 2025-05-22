package com.android.monu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.mapper.TransactionMapper
import com.android.monu.domain.model.Transaction
import com.android.monu.domain.model.TransactionConcise
import com.android.monu.domain.model.TransactionMonthlyAmount
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<TransactionConcise>> {
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
                    TransactionMapper.transactionConciseEntityToDomain(entity)
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

    override fun getTransactionMonthlyAmount(year: Int): Flow<List<TransactionMonthlyAmount>> {
        return transactionDao.getTransactionMonthlyAmount(year).map { entityList ->
            entityList.map { entity ->
                TransactionMapper.transactionMonthlyAmountEntityToDomain(entity)
            }
        }
    }

    override suspend fun insertTransaction(transaction: Transaction): Result<Long> {
        return try {
            val result = transactionDao.insertTransaction(
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