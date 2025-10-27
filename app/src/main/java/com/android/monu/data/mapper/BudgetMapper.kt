package com.android.monu.data.mapper

import com.android.monu.data.local.entity.room.BudgetEntity
import com.android.monu.domain.model.BudgetState

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
