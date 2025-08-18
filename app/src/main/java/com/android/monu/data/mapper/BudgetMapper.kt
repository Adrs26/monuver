package com.android.monu.data.mapper

import com.android.monu.data.local.entity.room.BudgetEntity
import com.android.monu.domain.model.budget.Budget

object BudgetMapper {
    fun budgetEntityToDomain(
        budgetEntity: BudgetEntity
    ): Budget {
        return Budget(
            id = budgetEntity.id,
            category = budgetEntity.category,
            period = budgetEntity.period,
            startDate = budgetEntity.startDate,
            endDate = budgetEntity.endDate,
            maxAmount = budgetEntity.maxAmount,
            usedAmount = budgetEntity.usedAmount,
            isActive = budgetEntity.isActive,
            isOverflowAllowed = budgetEntity.isOverflowAllowed,
            isAutoUpdate = budgetEntity.isAutoUpdate
        )
    }

    fun budgetDomainToEntity(
        budget: Budget
    ): BudgetEntity {
        return BudgetEntity(
            category = budget.category,
            period = budget.period,
            startDate = budget.startDate,
            endDate = budget.endDate,
            maxAmount = budget.maxAmount,
            usedAmount = budget.usedAmount,
            isActive = budget.isActive,
            isOverflowAllowed = budget.isOverflowAllowed,
            isAutoUpdate = budget.isAutoUpdate
        )
    }

    fun budgetDomainToEntityForUpdate(
        budget: Budget
    ): BudgetEntity {
        return BudgetEntity(
            id = budget.id,
            category = budget.category,
            period = budget.period,
            startDate = budget.startDate,
            endDate = budget.endDate,
            maxAmount = budget.maxAmount,
            usedAmount = budget.usedAmount,
            isActive = budget.isActive,
            isOverflowAllowed = budget.isOverflowAllowed,
            isAutoUpdate = budget.isAutoUpdate
        )
    }
}