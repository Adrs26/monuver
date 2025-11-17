package com.android.monuver.core.data.repository

import com.android.monuver.core.data.database.dao.AccountDao
import com.android.monuver.core.data.database.dao.BillDao
import com.android.monuver.core.data.database.dao.BudgetDao
import com.android.monuver.core.data.database.dao.TransactionDao
import com.android.monuver.core.data.mapper.toDomain
import com.android.monuver.core.data.mapper.toEntity
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoreRepositoryImpl(
    private val accountDao: AccountDao,
    private val billDao: BillDao,
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : CoreRepository {

    override fun getActiveAccounts(): Flow<List<AccountState>> {
        return accountDao.getActiveAccounts().map { accounts ->
            accounts.map { it.toDomain() }
        }
    }

    override suspend fun getAccountBalance(accountId: Int): Long? {
        return accountDao.getAccountBalance(accountId)
    }

    override fun getDistinctTransactionYears(): Flow<List<Int>> {
        return transactionDao.getDistinctTransactionYears()
    }

    override fun getTotalBudgetMaxAmount(): Flow<Long> {
        return budgetDao.getTotalBudgetMaxAmount()
    }

    override fun getTotalBudgetUsedAmount(): Flow<Long> {
        return budgetDao.getTotalBudgetUsedAmount()
    }

    override fun getAllActiveBudgets(): Flow<List<BudgetState>> {
        return budgetDao.getAllActiveBudgets().map { budgets ->
            budgets.map { it.toDomain() }
        }
    }

    override suspend fun createNewBudget(budgetState: BudgetState) {
        budgetDao.createNewBudget(budgetState.toEntity())
    }

    override suspend fun updateBudgetStatusToInactive(category: Int) {
        budgetDao.updateBudgetStatusToInactive(category)
    }

    override suspend fun getBudgetForDate(
        category: Int,
        date: String
    ): BudgetState? {
        return budgetDao.getBudgetForDate(category, date)?.toDomain()
    }

    override suspend fun getAllUnpaidBills(): List<BillState> {
        return billDao.getAllUnpaidBills().map { it.toDomain() }
    }
}