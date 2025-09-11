package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.screen.billing.paybill.components.PayBillContentState
import com.android.monu.ui.feature.utils.Cycle
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.TransactionType
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class PayBillUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository,
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(bill: Bill, payBillState: PayBillContentState): DatabaseResultMessage {
        when {
            payBillState.title.isEmpty() -> return DatabaseResultMessage.EmptyTransactionTitle
            payBillState.childCategory == 0 -> return DatabaseResultMessage.EmptyTransactionCategory
            payBillState.amount == 0L -> return DatabaseResultMessage.EmptyTransactionAmount
            payBillState.date.isEmpty() -> return DatabaseResultMessage.EmptyTransactionDate
            payBillState.sourceId == 0 -> return DatabaseResultMessage.EmptyTransactionSource
        }

        val accountBalance = accountRepository.getAccountBalance(payBillState.sourceId)
        if (accountBalance == null || accountBalance < payBillState.amount) {
            return DatabaseResultMessage.InsufficientAccountBalance
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
            return DatabaseResultMessage.CurrentBudgetAmountExceedsMaximumLimit
        }

        val (month, year) = DateHelper.getMonthAndYear(payBillState.date)
        val transaction = Transaction(
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
            isLocked = true
        )

        val isRecurring = (bill.period == 1) || (bill.nowPaidPeriod < (bill.fixPeriod ?: 0))

        val newBill = Bill(
            title = bill.title,
            dueDate = getNewDueDate(bill.cycle, bill.dueDate),
            paidDate = null,
            timeStamp = System.currentTimeMillis(),
            amount = bill.amount,
            isRecurring = bill.isRecurring,
            cycle = bill.cycle,
            period = bill.period,
            fixPeriod = bill.fixPeriod,
            isPaid = false,
            nowPaidPeriod = bill.nowPaidPeriod + 1
        )

        financeRepository.payBill(
            billId = bill.id,
            billPaidDate = payBillState.date,
            transaction = transaction,
            isRecurring = isRecurring,
            bill = newBill
        )
        return DatabaseResultMessage.PayBillSuccess
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