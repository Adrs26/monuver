package com.android.monu.domain.usecase.bill

import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.repository.BillRepository
import com.android.monu.ui.feature.screen.billing.addBill.components.AddBillContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class CreateBillUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(billState: AddBillContentState): DatabaseResultMessage {
        when {
            billState.title.isEmpty() -> return DatabaseResultMessage.EmptyBillTitle
            billState.date.isEmpty() -> return DatabaseResultMessage.EmptyBillDate
            billState.amount == 0L -> return DatabaseResultMessage.EmptyBillAmount
            billState.isRecurring && billState.period == 2 && billState.fixPeriod.isEmpty() ->
                return DatabaseResultMessage.EmptyBillFixPeriod
        }

        val bill = Bill(
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
        return DatabaseResultMessage.CreateBillSuccess
    }
}