package com.android.monu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monu.data.local.dao.BudgetDao
import com.android.monu.data.mapper.toDomain
import com.android.monu.data.mapper.toEntity
import com.android.monu.data.mapper.toEntityForUpdate
import com.android.monu.domain.model.BudgetState
import com.android.monu.domain.repository.BudgetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetRepositoryImpl(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override fun getAllActiveBudgets(): Flow<List<BudgetState>> {
        return budgetDao.getAllActiveBudgets().map { budgets ->
            budgets.map { it.toDomain() }
        }
    }

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

    override fun getTotalBudgetMaxAmount(): Flow<Long> {
        return budgetDao.getTotalBudgetMaxAmount()
    }

    override fun getTotalBudgetUsedAmount(): Flow<Long> {
        return budgetDao.getTotalBudgetUsedAmount()
    }

    override suspend fun isBudgetExists(category: Int): Boolean {
        return budgetDao.isBudgetExists(category)
    }

    override suspend fun createNewBudget(budgetState: BudgetState): Long {
        return budgetDao.createNewBudget(budgetState.toEntity())
    }

    override suspend fun getBudgetForDate(
        category: Int,
        date: String
    ): BudgetState? {
        return budgetDao.getBudgetForDate(category, date)?.toDomain()
    }

    override suspend fun getBudgetUsagePercentage(category: Int): Float {
        return budgetDao.getBudgetUsagePercentage(category)
    }

    override suspend fun updateBudgetStatusToInactive(category: Int) {
        budgetDao.updateBudgetStatusToInactive(category)
    }

    override suspend fun deleteBudgetById(budgetId: Long) {
        budgetDao.deleteBudgetById(budgetId)
    }

    override suspend fun updateBudget(budgetState: BudgetState) {
        budgetDao.updateBudget(budgetState.toEntityForUpdate())
    }

    override suspend fun getAllBudgets(): List<BudgetState> {
        return budgetDao.getAllBudgets().map { it.toDomain() }
    }
}