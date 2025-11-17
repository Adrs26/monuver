package com.android.monuver.core.data.mapper

import com.android.monuver.core.data.database.entity.room.BillEntity
import com.android.monuver.core.domain.model.BillState

fun BillEntity.toDomain() = BillState(
    id = id,
    parentId = parentId,
    title = title,
    dueDate = dueDate,
    paidDate = paidDate,
    timeStamp = timeStamp,
    amount = amount,
    isRecurring = isRecurring,
    cycle = cycle,
    period = period,
    fixPeriod = fixPeriod,
    isPaid = isPaid,
    nowPaidPeriod = nowPaidPeriod,
    isPaidBefore = isPaidBefore
)

fun BillState.toEntity() = BillEntity(
    parentId = parentId,
    title = title,
    dueDate = dueDate,
    paidDate = paidDate,
    timeStamp = timeStamp,
    amount = amount,
    isRecurring = isRecurring,
    cycle = cycle,
    period = period,
    fixPeriod = fixPeriod,
    isPaid = isPaid,
    nowPaidPeriod = nowPaidPeriod,
    isPaidBefore = isPaidBefore
)

fun BillState.toEntityForUpdate() = BillEntity(
    id = id,
    parentId = parentId,
    title = title,
    dueDate = dueDate,
    paidDate = paidDate,
    timeStamp = timeStamp,
    amount = amount,
    isRecurring = isRecurring,
    cycle = cycle,
    period = period,
    fixPeriod = fixPeriod,
    isPaid = isPaid,
    nowPaidPeriod = nowPaidPeriod,
    isPaidBefore = isPaidBefore
)
