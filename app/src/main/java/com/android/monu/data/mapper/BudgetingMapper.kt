package com.android.monu.data.mapper

import com.android.monu.data.local.entity.room.BudgetingEntity
import com.android.monu.domain.model.budgeting.Budgeting

object BudgetingMapper {
    fun budgetingEntityToDomain(
        budgetingEntity: BudgetingEntity
    ): Budgeting {
        return Budgeting(
            id = budgetingEntity.id,
            category = budgetingEntity.category,
            startDate = budgetingEntity.startDate,
            endDate = budgetingEntity.endDate,
            maxAmount = budgetingEntity.maxAmount,
            usedAmount = budgetingEntity.usedAmount,
            isActive = budgetingEntity.isActive,
            isOverflowAllowed = budgetingEntity.isOverflowAllowed,
            isAutoUpdate = budgetingEntity.isAutoUpdate
        )
    }

    fun budgetingDomainToEntity(
        budgeting: Budgeting
    ): BudgetingEntity {
        return BudgetingEntity(
            category = budgeting.category,
            startDate = budgeting.startDate,
            endDate = budgeting.endDate,
            maxAmount = budgeting.maxAmount,
            usedAmount = budgeting.usedAmount,
            isActive = budgeting.isActive,
            isOverflowAllowed = budgeting.isOverflowAllowed,
            isAutoUpdate = budgeting.isAutoUpdate
        )
    }
}