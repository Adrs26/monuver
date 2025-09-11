package com.android.monu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monu.data.local.dao.BudgetDao
import com.android.monu.data.mapper.BudgetMapper
import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.repository.BudgetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetRepositoryImpl(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override fun getAllActiveBudgets(): Flow<List<Budget>> {
        return budgetDao.getAllActiveBudgets().map { budgets ->
            budgets.map { budget ->
                BudgetMapper.budgetEntityToDomain(budget)
            }
        }
    }

    override fun getAllInactiveBudgets(scope: CoroutineScope): Flow<PagingData<Budget>> {
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
                pagingData.map { budget ->
                    BudgetMapper.budgetEntityToDomain(budget)
                }
            }.cachedIn(scope)
    }

    override fun getBudgetById(id: Long): Flow<Budget?> {
        return budgetDao.getBudgetById(id).map { budget ->
            budget?.let { BudgetMapper.budgetEntityToDomain(it) }
        }
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

    override suspend fun createNewBudget(budget: Budget): Long {
        return budgetDao.createNewBudget(BudgetMapper.budgetDomainToEntity(budget))
    }

    override suspend fun getBudgetForDate(
        category: Int,
        date: String
    ): Budget? {
        return budgetDao.getBudgetForDate(category, date)?.let { budget ->
            BudgetMapper.budgetEntityToDomain(budget)
        }
    }

    override suspend fun getBudgetUsagePercentage(category: Int): Float {
        return budgetDao.getBudgetUsagePercentage(category)
    }

    override suspend fun updateBudgetStatusToInactive(category: Int) {
        budgetDao.updateBudgetStatusToInactive(category)
    }

    override suspend fun deleteBudgetById(id: Long) {
        budgetDao.deleteBudgetById(id)
    }

    override suspend fun updateBudget(budget: Budget) {
        budgetDao.updateBudget(BudgetMapper.budgetDomainToEntityForUpdate(budget))
    }
}