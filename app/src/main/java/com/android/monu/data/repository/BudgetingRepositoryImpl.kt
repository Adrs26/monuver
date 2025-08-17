package com.android.monu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monu.data.local.dao.BudgetingDao
import com.android.monu.data.mapper.BudgetingMapper
import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.repository.BudgetingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetingRepositoryImpl(
    private val budgetingDao: BudgetingDao
) : BudgetingRepository {

    override fun getAllActiveBudgets(): Flow<List<Budgeting>> {
        return budgetingDao.getAllActiveBudgets().map { entityList ->
            entityList.map { entity ->
                BudgetingMapper.budgetingEntityToDomain(entity)
            }
        }
    }

    override fun getAllInactiveBudgets(scope: CoroutineScope): Flow<PagingData<Budgeting>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                budgetingDao.getAllInactiveBudgets()
            }
        ).flow
            .map { pagingData ->
                pagingData.map { entity ->
                    BudgetingMapper.budgetingEntityToDomain(entity)
                }
            }.cachedIn(scope)
    }

    override fun getBudgetingById(id: Long): Flow<Budgeting?> {
        return budgetingDao.getBudgetingById(id).map { entity ->
            BudgetingMapper.budgetingEntityToDomain(entity ?: return@map null)
        }
    }

    override fun getTotalBudgetingMaxAmount(): Flow<Long> {
        return budgetingDao.getTotalBudgetingMaxAmount()
    }

    override fun getTotalBudgetingUsedAmount(): Flow<Long> {
        return budgetingDao.getTotalBudgetingUsedAmount()
    }

    override suspend fun isBudgetingExists(category: Int): Boolean {
        return budgetingDao.isBudgetingExists(category)
    }

    override suspend fun createNewBudgeting(budgeting: Budgeting): Long {
        return budgetingDao.createNewBudgeting(
            BudgetingMapper.budgetingDomainToEntity(budgeting)
        )
    }

    override suspend fun getBudgetingForDate(
        category: Int,
        date: String
    ): Budgeting? {
        return budgetingDao.getBudgetingForDate(category, date)?.let { entity ->
            BudgetingMapper.budgetingEntityToDomain(entity)
        }
    }

    override suspend fun updateBudgetingStatusToInactive(category: Int) {
        budgetingDao.updateBudgetingStatusToInactive(category)
    }

    override suspend fun deleteBudgetingById(id: Long) {
        budgetingDao.deleteBudgetingById(id)
    }

    override suspend fun updateBudgeting(budgeting: Budgeting) {
        budgetingDao.updateBudgeting(BudgetingMapper.budgetingDomainToEntityForUpdate(budgeting))
    }
}