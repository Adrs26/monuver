package com.android.monu.domain.usecase.bill

import android.util.Log
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.repository.BillRepository
import com.android.monu.ui.feature.screen.bill.addbill.components.AddBillContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class CreateBillUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(billState: AddBillContentState): DatabaseResultMessage {
        when {
            billState.title.isEmpty() -> return DatabaseResultMessage.EmptyBillTitle
            billState.date.isEmpty() -> return DatabaseResultMessage.EmptyBillDate
            billState.amount == 0L -> return DatabaseResultMessage.EmptyBillAmount
            billState.isRecurring == true && billState.period == 2 && billState.fixPeriod.isEmpty() ->
                return DatabaseResultMessage.EmptyBillFixPeriod
        }

        val bill = Bill(
            title = billState.title,
            dueDate = billState.date,
            paidDate = null,
            amount = billState.amount,
            timeStamp = System.currentTimeMillis(),
            isRecurring = billState.isRecurring,
            cycle = if (billState.isRecurring == true) billState.cycle else null,
            period = if (billState.isRecurring == true) billState.period else null,
            fixPeriod = if (billState.isRecurring == true && billState.period == 2) billState.fixPeriod.toInt() else null,
            isPaid = false,
            nowPaidPeriod = 1
        )

        repository.createNewBill(bill)
        Log.d("AddBillScreen", "onAddNewBill: $bill")
        return DatabaseResultMessage.CreateBillSuccess
    }
}