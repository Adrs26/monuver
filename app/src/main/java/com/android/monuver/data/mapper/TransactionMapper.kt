package com.android.monuver.data.mapper

import com.android.monuver.data.local.entity.projection.Transaction
import com.android.monuver.data.local.entity.projection.TransactionCategorySummaryEntity
import com.android.monuver.data.local.entity.projection.TransactionSummaryEntity
import com.android.monuver.data.local.entity.room.TransactionEntity
import com.android.monuver.domain.model.TransactionCategorySummaryState
import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.model.TransactionSummaryState

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

fun Transaction.toDomain() = TransactionState(
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

fun TransactionState.toEntityProjection() = Transaction(
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

fun TransactionSummaryEntity.toDomain() = TransactionSummaryState(
    type = type,
    date = date,
    amount = amount
)

fun TransactionCategorySummaryEntity.toDomain() = TransactionCategorySummaryState(
    parentCategory = parentCategory,
    totalAmount = totalAmount
)
