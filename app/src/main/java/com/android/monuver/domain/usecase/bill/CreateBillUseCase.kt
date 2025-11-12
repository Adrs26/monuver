package com.android.monuver.domain.usecase.bill

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AddBillState
import com.android.monuver.domain.model.BillState
import com.android.monuver.domain.repository.BillRepository

class CreateBillUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(billState: AddBillState): DatabaseResultState {
        when {
            billState.title.isEmpty() -> return DatabaseResultState.EmptyBillTitle
            billState.date.isEmpty() -> return DatabaseResultState.EmptyBillDate
            billState.amount == 0L -> return DatabaseResultState.EmptyBillAmount
            billState.isRecurring && billState.period == 2 && billState.fixPeriod.isEmpty() ->
                return DatabaseResultState.EmptyBillFixPeriod
        }

        val bill = BillState(
            title = billState.title,
            dueDate = billState.date,
            paidDate = null,
            amount = billState.amount,
            timeStamp = System.currentTimeMillis(),
            isRecurring = billState.isRecurring,
            cycle = if (billState.isRecurring) billState.cycle else null,
            period = if (billState.isRecurring) billState.period else null,
            fixPeriod = if (billState.isRecurring && billState.period == 2) billState.fixPeriod.toInt() else null,
            isPaid = false,
            nowPaidPeriod = 1,
            isPaidBefore = false
        )

        val billId = repository.createNewBill(bill)
        repository.updateParentId(billId, billId)
        return DatabaseResultState.CreateBillSuccess
    }
}