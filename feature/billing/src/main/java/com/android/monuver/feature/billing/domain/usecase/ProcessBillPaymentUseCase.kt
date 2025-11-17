package com.android.monuver.feature.billing.domain.usecase

import com.android.monuver.feature.billing.domain.model.PayBillState
import com.android.monuver.feature.billing.domain.repository.BillRepository
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.repository.CoreRepository
import com.android.monuver.core.domain.util.Cycle
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionType
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

internal class ProcessBillPaymentUseCase(
    private val coreRepository: CoreRepository,
    private val billRepository: BillRepository
) {
    suspend operator fun invoke(
        billState: BillState,
        payBillState: PayBillState
    ): DatabaseResultState {
        when {
            payBillState.title.isEmpty() -> return DatabaseResultState.EmptyTransactionTitle
            payBillState.childCategory == 0 -> return DatabaseResultState.EmptyTransactionCategory
            payBillState.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
            payBillState.date.isEmpty() -> return DatabaseResultState.EmptyTransactionDate
            payBillState.sourceId == 0 -> return DatabaseResultState.EmptyTransactionSource
        }

        val accountBalance = coreRepository.getAccountBalance(payBillState.sourceId)
        if (accountBalance == null || accountBalance < payBillState.amount) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        val budget = coreRepository.getBudgetForDate(
            category = payBillState.parentCategory,
            date = payBillState.date
        )
        if (
            budget != null &&
            budget.usedAmount + payBillState.amount > budget.maxAmount &&
            !budget.isOverflowAllowed
        ) {
            return DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit
        }

        val (month, year) = DateHelper.getMonthAndYear(payBillState.date)
        val transactionState = TransactionState(
            title = payBillState.title,
            type = TransactionType.EXPENSE,
            parentCategory = payBillState.parentCategory,
            childCategory = payBillState.childCategory,
            date = payBillState.date,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = payBillState.amount,
            sourceId = payBillState.sourceId,
            sourceName = payBillState.sourceName,
            billId = billState.id,
            isLocked = true,
            isSpecialCase = true
        )

        val isRecurring = !billState.isPaidBefore && ((billState.period == 1) ||
                (billState.nowPaidPeriod < (billState.fixPeriod ?: 0)))

        val newBillState = BillState(
            parentId = billState.parentId,
            title = billState.title,
            dueDate = getNewDueDate(billState.cycle, billState.dueDate),
            paidDate = null,
            timeStamp = System.currentTimeMillis(),
            amount = billState.amount,
            isRecurring = billState.isRecurring,
            cycle = billState.cycle,
            period = billState.period,
            fixPeriod = billState.fixPeriod,
            isPaid = false,
            nowPaidPeriod = billState.nowPaidPeriod + 1,
            isPaidBefore = false
        )

        billRepository.processBillPayment(
            billId = billState.id,
            billPaidDate = payBillState.date,
            transactionState = transactionState,
            isRecurring = isRecurring,
            billState = newBillState
        )
        return DatabaseResultState.PayBillSuccess
    }

    private fun getNewDueDate(cycle: Int?, dueDate: String): String {
        val date = LocalDate.parse(dueDate)

        return when (cycle) {
            Cycle.YEARLY -> date.plus(1, DateTimeUnit.YEAR).toString()
            Cycle.MONTHLY -> date.plus(1, DateTimeUnit.MONTH).toString()
            Cycle.WEEKLY -> date.plus(7, DateTimeUnit.DAY).toString()
            else -> return ""
        }
    }
}