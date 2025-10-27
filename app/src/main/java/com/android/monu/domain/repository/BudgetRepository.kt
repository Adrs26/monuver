package com.android.monu.domain.repository

import androidx.paging.PagingData
import com.android.monu.domain.model.BudgetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    fun getAllActiveBudgets(): Flow<List<BudgetState>>

    fun getAllInactiveBudgets(scope: CoroutineScope): Flow<PagingData<BudgetState>>

    fun getBudgetById(budgetId: Long): Flow<BudgetState?>

    fun getTotalBudgetMaxAmount(): Flow<Long>

    fun getTotalBudgetUsedAmount(): Flow<Long>

    suspend fun isBudgetExists(category: Int): Boolean

    suspend fun createNewBudget(budgetState: BudgetState): Long

    suspend fun getBudgetForDate(category: Int, date: String): BudgetState?

    suspend fun getBudgetUsagePercentage(category: Int): Float

    suspend fun updateBudgetStatusToInactive(category: Int)

    suspend fun deleteBudgetById(budgetId: Long)

    suspend fun updateBudget(budgetState: BudgetState)

    suspend fun getAllBudgets(): List<BudgetState>
}