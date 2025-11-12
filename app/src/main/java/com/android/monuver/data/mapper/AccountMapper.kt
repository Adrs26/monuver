package com.android.monuver.data.mapper

import com.android.monuver.data.local.entity.projection.Account
import com.android.monuver.data.local.entity.room.AccountEntity
import com.android.monuver.domain.model.AccountState

fun AccountEntity.toDomain() = AccountState(
    id = id,
    name = name,
    type = type,
    balance = balance,
    isActive = isActive
)

fun Account.toDomain() = AccountState(
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

fun AccountState.toEntityProjection() = Account(
    id = id,
    name = name,
    type = type,
    balance = balance,
    isActive = isActive
)