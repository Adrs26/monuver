package com.android.monu.domain.repository

import androidx.paging.PagingData
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.model.transaction.TransactionMonthlyAmountOverview
import com.android.monu.domain.model.transaction.TransactionOverview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getTotalTransactionAmount(type: Int): Flow<Long?>

    fun getRecentTransactions(): Flow<List<Transaction>>

    fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<Transaction>>

    fun getTransactionById(transactionId: Long): Flow<Transaction?>

    fun getAvailableTransactionYears(): Flow<List<Int>>

    fun getTransactionMonthlyAmountOverview(
        month: Int,
        year: Int
    ): Flow<TransactionMonthlyAmountOverview>

    fun getMonthlyTransactionOverviewsByType(type: Int, year: Int): Flow<List<TransactionOverview>>

    suspend fun createNewTransaction(transaction: Transaction): Result<Long>

    suspend fun updateTransaction(transaction: Transaction): Result<Int>

    suspend fun deleteTransactionById(transactionId: Long): Result<Int>
}