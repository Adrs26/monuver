package com.android.monu.data.mapper

import com.android.monu.data.local.entity.room.AccountEntity
import com.android.monu.domain.model.AccountState

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