package com.android.monu.data.mapper

import com.android.monu.data.local.entity.projection.Saving
import com.android.monu.data.local.entity.room.SavingEntity
import com.android.monu.domain.model.SavingState

fun SavingEntity.toDomain() = SavingState(
    id = id,
    title = title,
    targetDate = targetDate,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    isActive = isActive
)

fun Saving.toDomain() = SavingState(
    id = id,
    title = title,
    targetDate = targetDate,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    isActive = isActive
)

fun SavingState.toEntity() = SavingEntity(
    title = title,
    targetDate = targetDate,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    isActive = isActive
)

fun SavingState.toEntityForUpdate() = SavingEntity(
    id = id,
    title = title,
    targetDate = targetDate,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    isActive = isActive
)

fun SavingState.toEntityProjection() = Saving(
    id = id,
    title = title,
    targetDate = targetDate,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    isActive = isActive
)
