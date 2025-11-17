package com.android.monuver.feature.transaction.domain.repository

import androidx.paging.PagingData
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.transaction.domain.common.BudgetStatusState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun createIncomeTransaction(transactionState: TransactionState)

    suspend fun createExpenseTransaction(transactionState: TransactionState)

    suspend fun createTransferTransaction(transactionState: TransactionState)

    suspend fun getBudgetUsagePercentage(category: Int): Float

    suspend fun deleteIncomeTransaction(transactionId: Long, sourceId: Int, amount: Long)

    suspend fun deleteExpenseTransaction(
        transactionId: Long,
        parentCategory: Int,
        date: String,
        sourceId: Int,
        amount: Long
    )

    suspend fun updateIncomeTransaction(transactionState: TransactionState, initialAmount: Long)

    suspend fun updateExpenseTransaction(
        transactionState: TransactionState,
        initialParentCategory: Int,
        initialDate: String,
        initialAmount: Long,
        budgetStatus: BudgetStatusState
    )

    fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<TransactionState>>

    fun getTransactionById(transactionId: Long): Flow<TransactionState?>
}