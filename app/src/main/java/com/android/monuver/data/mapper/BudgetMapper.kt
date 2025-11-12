package com.android.monuver.data.mapper

import com.android.monuver.data.local.entity.projection.Budget
import com.android.monuver.data.local.entity.room.BudgetEntity
import com.android.monuver.domain.model.BudgetState

fun BudgetEntity.toDomain() = BudgetState(
    id = id,
    category = category,
    cycle = cycle,
    startDate = startDate,
    endDate = endDate,
    maxAmount = maxAmount,
    usedAmount = usedAmount,
    isActive = isActive,
    isOverflowAllowed = isOverflowAllowed,
    isAutoUpdate = isAutoUpdate
)

fun Budget.toDomain() = BudgetState(
    id = id,
    category = category,
    cycle = cycle,
    startDate = startDate,
    endDate = endDate,
    maxAmount = maxAmount,
    usedAmount = usedAmount,
    isActive = isActive,
    isOverflowAllowed = isOverflowAllowed,
    isAutoUpdate = isAutoUpdate
)

fun BudgetState.toEntity() = BudgetEntity(
    category = category,
    cycle = cycle,
    startDate = startDate,
    endDate = endDate,
    maxAmount = maxAmount,
    usedAmount = usedAmount,
    isActive = isActive,
    isOverflowAllowed = isOverflowAllowed,
    isAutoUpdate = isAutoUpdate
)

fun BudgetState.toEntityForUpdate() = BudgetEntity(
    id = id,
    category = category,
    cycle = cycle,
    startDate = startDate,
    endDate = endDate,
    maxAmount = maxAmount,
    usedAmount = usedAmount,
    isActive = isActive,
    isOverflowAllowed = isOverflowAllowed,
    isAutoUpdate = isAutoUpdate
)

fun BudgetState.toEntityProjection() = Budget(
    id = id,
    category = category,
    cycle = cycle,
    startDate = startDate,
    endDate = endDate,
    maxAmount = maxAmount,
    usedAmount = usedAmount,
    isActive = isActive,
    isOverflowAllowed = isOverflowAllowed,
    isAutoUpdate = isAutoUpdate
)
