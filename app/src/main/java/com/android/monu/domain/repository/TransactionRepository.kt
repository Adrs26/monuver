package com.android.monu.domain.repository

import androidx.paging.PagingData
import com.android.monu.domain.model.TransactionCategorySummaryState
import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.model.TransactionSummaryState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getRecentTransactions(): Flow<List<TransactionState>>

    fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<TransactionState>>

    fun getTransactionById(transactionId: Long): Flow<TransactionState?>

    fun getTransactionsBySavingId(savingId: Long): Flow<List<TransactionState>>

    fun getTransactionsByParentCategoryAndDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<TransactionState>>

    fun getTransactionsByParentCategoryAndMonthAndYear(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionState>>

    fun getDistinctTransactionYears(): Flow<List<Int>>

    fun getTotalMonthlyTransactionAmount(type: Int, month: Int, year: Int): Flow<Long?>

    fun getAverageDailyTransactionAmountInMonth(type: Int, month: Int, year: Int): Flow<Double?>

    fun getGroupedMonthlyTransactionAmountByParentCategory(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummaryState>>

    fun getTransactionsInRange(startDate: String, endDate: String): Flow<List<TransactionSummaryState>>

    suspend fun getTotalTransactionAmountInDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Long

    suspend fun getTransactionsBySavingIdSuspend(savingId: Long): List<TransactionState>

    suspend fun getAllTransactions(): List<TransactionState>

    suspend fun getTransactionsInRangeByDateAsc(startDate: String, endDate: String): List<TransactionState>

    suspend fun getTransactionsInRangeByDateDesc(startDate: String, endDate: String): List<TransactionState>

    suspend fun getTransactionsInRangeByDateAscWithType(startDate: String, endDate: String): List<TransactionState>

    suspend fun getTransactionsInRangeByDateDescWithType(startDate: String, endDate: String): List<TransactionState>

    suspend fun getTotalIncomeTransactionInRange(startDate: String, endDate: String): Long?

    suspend fun getTotalExpenseTransactionInRange(startDate: String, endDate: String): Long?
}