package com.android.monuver.feature.budgeting.domain.repository

import androidx.paging.PagingData
import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.model.TransactionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

internal interface BudgetRepository {

    fun getAllInactiveBudgets(scope: CoroutineScope): Flow<PagingData<BudgetState>>

    fun getBudgetById(budgetId: Long): Flow<BudgetState?>

    suspend fun isBudgetExists(category: Int): Boolean

    suspend fun deleteBudgetById(budgetId: Long)

    suspend fun updateBudget(budgetState: BudgetState)

    fun getTransactionsByParentCategoryAndDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<TransactionState>>

    suspend fun getTotalTransactionAmountInDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Long
}