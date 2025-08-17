package com.android.monu.domain.repository

import androidx.paging.PagingData
import com.android.monu.domain.model.budgeting.Budgeting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface BudgetingRepository {

    fun getAllActiveBudgets(): Flow<List<Budgeting>>

    fun getAllInactiveBudgets(scope: CoroutineScope): Flow<PagingData<Budgeting>>

    fun getBudgetingById(id: Long): Flow<Budgeting?>

    fun getTotalBudgetingMaxAmount(): Flow<Long>

    fun getTotalBudgetingUsedAmount(): Flow<Long>

    suspend fun isBudgetingExists(category: Int): Boolean

    suspend fun createNewBudgeting(budgeting: Budgeting): Long

    suspend fun getBudgetingForDate(category: Int, date: String): Budgeting?

    suspend fun updateBudgetingStatusToInactive(category: Int)

    suspend fun deleteBudgetingById(id: Long)

    suspend fun updateBudgeting(budgeting: Budgeting)
}