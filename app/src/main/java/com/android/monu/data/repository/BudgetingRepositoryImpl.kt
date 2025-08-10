package com.android.monu.data.repository

import com.android.monu.data.local.dao.BudgetingDao
import com.android.monu.data.mapper.BudgetingMapper
import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.repository.BudgetingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetingRepositoryImpl(
    private val budgetingDao: BudgetingDao
) : BudgetingRepository {

    override fun getAllBudgets(): Flow<List<Budgeting>> {
        return budgetingDao.getAllBudgets().map { entityList ->
            entityList.map { entity ->
                BudgetingMapper.budgetingEntityToDomain(entity)
            }
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
}