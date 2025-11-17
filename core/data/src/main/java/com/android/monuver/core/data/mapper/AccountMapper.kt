package com.android.monuver.core.data.mapper

import com.android.monuver.core.data.database.entity.room.AccountEntity
import com.android.monuver.core.domain.model.AccountState

fun AccountEntity.toDomain() = AccountState(
    id = id,
    name = name,
    type = type,
    balance = balance,
    isActive = isActive
)

fun AccountState.toEntity() = AccountEntity(
    name = name,
    type = type,
    balance = balance,
    isActive = isActive
)

fun AccountState.toEntityForUpdate() = AccountEntity(
    id = id,
    name = name,
    type = type,
    balance = balance,
    isActive = isActive
)