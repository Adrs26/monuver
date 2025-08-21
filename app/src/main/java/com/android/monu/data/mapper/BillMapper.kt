package com.android.monu.data.mapper

import com.android.monu.data.local.entity.room.BillEntity
import com.android.monu.domain.model.bill.Bill

object BillMapper {
    fun billEntityToDomain(
        billEntity: BillEntity
    ): Bill {
        return Bill(
            id = billEntity.id,
            title = billEntity.title,
            dueDate = billEntity.dueDate,
            paidDate = billEntity.paidDate,
            timeStamp = billEntity.timeStamp,
            amount = billEntity.amount,
            isRecurring = billEntity.isRecurring,
            cycle = billEntity.cycle,
            period = billEntity.period,
            fixPeriod = billEntity.fixPeriod,
            isPaid = billEntity.isPaid,
            nowPaidPeriod = billEntity.nowPaidPeriod
        )
    }

    fun billDomainToEntity(
        bill: Bill
    ): BillEntity {
        return BillEntity(
            title = bill.title,
            dueDate = bill.dueDate,
            paidDate = bill.paidDate,
            timeStamp = bill.timeStamp,
            amount = bill.amount,
            isRecurring = bill.isRecurring,
            cycle = bill.cycle,
            period = bill.period,
            fixPeriod = bill.fixPeriod,
            isPaid = bill.isPaid,
            nowPaidPeriod = bill.nowPaidPeriod
        )
    }
}