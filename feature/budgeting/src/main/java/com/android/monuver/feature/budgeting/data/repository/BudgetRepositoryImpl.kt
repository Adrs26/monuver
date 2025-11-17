package com.android.monuver.feature.budgeting.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monuver.core.data.database.dao.BudgetDao
import com.android.monuver.core.data.database.dao.TransactionDao
import com.android.monuver.core.data.mapper.toDomain
import com.android.monuver.core.data.mapper.toEntityForUpdate
import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.budgeting.domain.repository.BudgetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BudgetRepositoryImpl(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : BudgetRepository {

    override fun getAllInactiveBudgets(scope: CoroutineScope): Flow<PagingData<BudgetState>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                budgetDao.getAllInactiveBudgets()
            }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomain() }
            }.cachedIn(scope)
    }

    override fun getBudgetById(budgetId: Long): Flow<BudgetState?> {
        return budgetDao.getBudgetById(budgetId).map { it?.toDomain() }
    }

    override suspend fun isBudgetExists(category: Int): Boolean {
        return budgetDao.isBudgetExists(category)
    }

    override suspend fun deleteBudgetById(budgetId: Long) {
        budgetDao.deleteBudgetById(budgetId)
    }

    override suspend fun updateBudget(budgetState: BudgetState) {
        budgetDao.updateBudget(budgetState.toEntityForUpdate())
    }

    override fun getTransactionsByParentCategoryAndDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<TransactionState>> {
        return transactionDao.getTransactionsByParentCategoryAndDateRange(
            category, startDate, endDate
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override suspend fun getTotalTransactionAmountInDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Long {
        return transactionDao.getTotalTransactionAmountInDateRange(category, startDate, endDate)
    }
}