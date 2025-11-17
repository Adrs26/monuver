package com.android.monuver.core.domain.repository

import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.model.BudgetState
import kotlinx.coroutines.flow.Flow

interface CoreRepository {

    fun getActiveAccounts(): Flow<List<AccountState>>

    suspend fun getAccountBalance(accountId: Int): Long?

    fun getDistinctTransactionYears(): Flow<List<Int>>

    fun getTotalBudgetMaxAmount(): Flow<Long>

    fun getTotalBudgetUsedAmount(): Flow<Long>

    fun getAllActiveBudgets(): Flow<List<BudgetState>>

    suspend fun updateBudgetStatusToInactive(category: Int)

    suspend fun createNewBudget(budgetState: BudgetState)

    suspend fun getBudgetForDate(category: Int, date: String): BudgetState?

    suspend fun getAllUnpaidBills(): List<BillState>
}