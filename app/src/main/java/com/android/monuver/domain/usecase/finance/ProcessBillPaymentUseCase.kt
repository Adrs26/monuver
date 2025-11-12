package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.BillState
import com.android.monuver.domain.model.PayBillState
import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.AccountRepository
import com.android.monuver.domain.repository.BudgetRepository
import com.android.monuver.domain.repository.FinanceRepository
import com.android.monuver.utils.Cycle
import com.android.monuver.utils.DateHelper
import com.android.monuver.utils.TransactionType
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class ProcessBillPaymentUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository,
    private val budgetRepository: BudgetRepository
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

        val accountBalance = accountRepository.getAccountBalance(payBillState.sourceId)
        if (accountBalance == null || accountBalance < payBillState.amount) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        val budget = budgetRepository.getBudgetForDate(
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

        val isRecurring = !billState.isPaidBefore && ((billState.period == 1) || (billState.nowPaidPeriod < (billState.fixPeriod ?: 0)))

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

        financeRepository.processBillPayment(
            billId = billState.id,
            billPaidDate = payBillState.date,
            transactionState = transactionState,
            isRecurring = isRecurring,
            billState = newBillState
        )
        return DatabaseResultState.PayBillSuccess
    }

    private fun getNewDueDate(cycle: Int?, dueDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dueDate, formatter)

        return when (cycle) {
            Cycle.YEARLY -> date.plusYears(1).format(formatter)
            Cycle.MONTHLY -> date.plusMonths(1).format(formatter)
            Cycle.WEEKLY -> date.plusWeeks(1).format(formatter)
            else -> ""
        }
    }
}