package com.android.monuver.feature.billing.domain.usecase

import com.android.monuver.feature.billing.domain.model.EditBillState
import com.android.monuver.feature.billing.domain.repository.BillRepository
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.BillState

internal class UpdateBillUseCase(
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
        repository.updateBillPeriodByParentId(
            period = bill.period,
            fixPeriod = bill.fixPeriod,
            parentId = bill.parentId
        )
        return DatabaseResultState.UpdateBillSuccess
    }
}