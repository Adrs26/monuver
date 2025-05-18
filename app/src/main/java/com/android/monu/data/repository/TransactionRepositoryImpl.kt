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
}