package com.android.monuver.data.mapper

import com.android.monuver.data.local.entity.projection.Saving
import com.android.monuver.data.local.entity.room.SavingEntity
import com.android.monuver.domain.model.SavingState

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
