package com.android.monu.domain.repository

import androidx.paging.PagingData
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.model.transaction.TransactionCategorySummary
import com.android.monu.domain.model.transaction.TransactionSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getRecentTransactions(): Flow<List<Transaction>>

    fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<Transaction>>

    fun getTransactionById(transactionId: Long): Flow<Transaction?>

    fun getTransactionsBySaveId(saveId: Long): Flow<List<Transaction>>

    fun getTransactionsByParentCategoryAndDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<Transaction>>

    fun getTransactionsByParentCategoryAndMonthAndYear(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<Transaction>>

    fun getDistinctTransactionYears(): Flow<List<Int>>

    fun getTotalMonthlyTransactionAmount(type: Int, month: Int, year: Int): Flow<Long?>

    fun getAverageDailyTransactionAmountInMonth(type: Int, month: Int, year: Int): Flow<Double?>

    fun getGroupedMonthlyTransactionAmountByParentCategory(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummary>>

    fun getTransactionsInRange(startDate: String, endDate: String): Flow<List<TransactionSummary>>

    suspend fun getTotalTransactionAmountInDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Long
}