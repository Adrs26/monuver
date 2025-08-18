package com.android.monu.domain.repository

import androidx.paging.PagingData
import com.android.monu.domain.model.budget.Budget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    fun getAllActiveBudgets(): Flow<List<Budget>>

    fun getAllInactiveBudgets(scope: CoroutineScope): Flow<PagingData<Budget>>

    fun getBudgetById(id: Long): Flow<Budget?>

    fun getTotalBudgetMaxAmount(): Flow<Long>

    fun getTotalBudgetUsedAmount(): Flow<Long>

    suspend fun isBudgetExists(category: Int): Boolean

    suspend fun createNewBudget(budget: Budget): Long

    suspend fun getBudgetForDate(category: Int, date: String): Budget?

    suspend fun getBudgetUsagePercentage(category: Int): Float

    suspend fun updateBudgetStatusToInactive(category: Int)

    suspend fun deleteBudgetById(id: Long)

    suspend fun updateBudget(budget: Budget)
}