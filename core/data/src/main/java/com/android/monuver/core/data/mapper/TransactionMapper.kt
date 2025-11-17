package com.android.monuver.core.data.mapper

import com.android.monuver.core.data.database.entity.room.TransactionEntity
import com.android.monuver.core.domain.model.TransactionState

fun TransactionEntity.toDomain() = TransactionState(
    id = id,
    title = title,
    type = type,
    parentCategory = parentCategory,
    childCategory = childCategory,
    date = date,
    month = month,
    year = year,
    timeStamp = timeStamp,
    amount = amount,
    sourceId = sourceId,
    sourceName = sourceName,
    destinationId = destinationId,
    destinationName = destinationName,
    saveId = saveId,
    billId = billId,
    isLocked = isLocked,
    isSpecialCase = isSpecialCase
)

fun TransactionState.toEntity() = TransactionEntity(
    title = title,
    type = type,
    parentCategory = parentCategory,
    childCategory = childCategory,
    date = date,
    month = month,
    year = year,
    timeStamp = timeStamp,
    amount = amount,
    sourceId = sourceId,
    sourceName = sourceName,
    destinationId = destinationId,
    destinationName = destinationName,
    saveId = saveId,
    billId = billId,
    isLocked = isLocked,
    isSpecialCase = isSpecialCase
)

fun TransactionState.toEntityForUpdate() = TransactionEntity(
    id = id,
    title = title,
    type = type,
    parentCategory = parentCategory,
    childCategory = childCategory,
    date = date,
    month = month,
    year = year,
    timeStamp = timeStamp,
    amount = amount,
    sourceId = sourceId,
    sourceName = sourceName,
    destinationId = destinationId,
    destinationName = destinationName,
    saveId = saveId,
    billId = billId,
    isLocked = isLocked,
    isSpecialCase = isSpecialCase
)
