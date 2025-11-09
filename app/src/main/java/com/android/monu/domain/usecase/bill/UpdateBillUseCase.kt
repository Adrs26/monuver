package com.android.monu.domain.usecase.bill

import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.BillState
import com.android.monu.domain.model.EditBillState
import com.android.monu.domain.repository.BillRepository

class UpdateBillUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(billState: EditBillState): DatabaseResultState {
        when {
            billState.title.isEmpty() -> return DatabaseResultState.EmptyBillTitle
            billState.date.isEmpty() -> return DatabaseResultState.EmptyBillDate
            billState.amount == 0L -> return DatabaseResultState.EmptyBillAmount
            billState.isRecurring && billState.period == 2 && billState.fixPeriod.isEmpty() ->
                return DatabaseResultState.EmptyBillFixPeriod
            billState.isRecurring && billState.fixPeriod.toInt() < billState.nowPaidPeriod ->
                return DatabaseResultState.InvalidBillFixPeriod
        }

        val bill = BillState(
            id = billState.id,
            parentId = billState.parentId,
            title = billState.title,
            dueDate = billState.date,
            paidDate = null,
            amount = billState.amount,
            timeStamp = billState.timeStamp,
            isRecurring = billState.isRecurring,
            cycle = if (billState.isRecurring) billState.cycle else null,
            period = if (billState.isRecurring) billState.period else null,
            fixPeriod = if (billState.isRecurring && billState.period == 2) billState.fixPeriod.toInt() else null,
            isPaid = false,
            nowPaidPeriod = billState.nowPaidPeriod,
            isPaidBefore = billState.isPaidBefore
        )

        repository.updateBill(bill)
        repository.updateBillPeriodByParentId(bill.period, bill.fixPeriod, bill.parentId)
        return DatabaseResultState.UpdateBillSuccess
    }
}