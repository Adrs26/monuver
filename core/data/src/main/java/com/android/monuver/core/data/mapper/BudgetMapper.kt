package com.android.monuver.core.data.mapper

import com.android.monuver.core.data.database.entity.room.BudgetEntity
import com.android.monuver.core.domain.model.BudgetState

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
