package com.android.monu.domain.usecase.bill

import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.repository.BillRepository
import com.android.monu.ui.feature.screen.billing.editbill.components.EditBillContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class UpdateBillUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(billState: EditBillContentState): DatabaseResultMessage {
        when {
            billState.title.isEmpty() -> return DatabaseResultMessage.EmptyBillTitle
            billState.date.isEmpty() -> return DatabaseResultMessage.EmptyBillDate
            billState.amount == 0L -> return DatabaseResultMessage.EmptyBillAmount
            billState.isRecurring && billState.period == 2 && billState.fixPeriod.isEmpty() ->
                return DatabaseResultMessage.EmptyBillFixPeriod
            billState.fixPeriod.toInt() < billState.nowPaidPeriod ->
                return DatabaseResultMessage.InvalidBillFixPeriod
        }

        val bill = Bill(
            id = billState.id,
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
            nowPaidPeriod = billState.nowPaidPeriod
        )

        repository.updateBill(bill)
        return DatabaseResultMessage.UpdateBillSuccess
    }
}