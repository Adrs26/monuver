package com.android.monu.data.mapper

import com.android.monu.data.local.entity.projection.Bill
import com.android.monu.data.local.entity.room.BillEntity
import com.android.monu.domain.model.BillState

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

fun Bill.toDomain() = BillState(
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

fun BillState.toEntityProjection() = Bill(
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
