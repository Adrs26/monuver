package com.android.monu.domain.repository

import androidx.paging.PagingData
import com.android.monu.domain.model.Transaction
import com.android.monu.domain.model.TransactionConcise
import com.android.monu.domain.model.TransactionMonthlyAmount
import com.android.monu.domain.model.TransactionOverview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<TransactionConcise>>

    fun getTransactionById(transactionId: Long): Flow<Transaction?>

    fun getAvailableTransactionYears(): Flow<List<Int>>

    fun getTransactionMonthlyAmount(year: Int): Flow<List<TransactionMonthlyAmount>>

    fun getAverageTransactionAmountPerDay(type: Int): Flow<Double>

    fun getAverageTransactionAmountPerMonth(type: Int): Flow<Double>

    fun getAverageTransactionAmountPerYear(type: Int): Flow<Double>

    fun getMonthlyTransactionOverviewByType(type: Int, year: Int): Flow<List<TransactionOverview>>

    fun getMonthlyTransactionBalance(year: Int): Flow<List<TransactionOverview>>

    suspend fun insertTransaction(transaction: Transaction): Result<Long>

    suspend fun updateTransaction(transaction: Transaction): Result<Int>

    suspend fun deleteTransactionById(transactionId: Long): Result<Int>
}