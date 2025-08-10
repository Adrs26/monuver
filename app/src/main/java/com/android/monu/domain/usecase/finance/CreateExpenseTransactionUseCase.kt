package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BudgetingRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.screen.transaction.addtransaction.components.AddTransactionContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.presentation.utils.DateHelper

class CreateExpenseTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository,
    private val budgetingRepository: BudgetingRepository
) {

    suspend operator fun invoke(
        transactionState: AddTransactionContentState
    ): DatabaseResultMessage {
        when {
            transactionState.title.isEmpty() -> return DatabaseResultMessage.EmptyTransactionTitle
            transactionState.childCategory == 0 -> return DatabaseResultMessage.EmptyTransactionCategory
            transactionState.date.isEmpty() -> return DatabaseResultMessage.EmptyTransactionDate
            transactionState.amount == 0L -> return DatabaseResultMessage.EmptyTransactionAmount
            transactionState.sourceId == 0 -> return DatabaseResultMessage.EmptyTransactionSource
        }

        val (month, year) = DateHelper.getMonthAndYear(transactionState.date)
        val transaction = Transaction(
            title = transactionState.title,
            type = transactionState.type,
            parentCategory = transactionState.parentCategory,
            childCategory = transactionState.childCategory,
            date = transactionState.date,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = transactionState.amount,
            sourceId = transactionState.sourceId,
            sourceName = transactionState.sourceName,
        )

        val accountBalance = accountRepository.getAccountBalance(transaction.sourceId)
        val budgeting = budgetingRepository.getBudgetingForDate(
            category = transaction.parentCategory,
            date = transaction.date
        )

        if (accountBalance == null || accountBalance < transaction.amount) {
            return DatabaseResultMessage.InsufficientAccountBalance
        }

        if (
            budgeting != null &&
            budgeting.usedAmount + transaction.amount > budgeting.maxAmount &&
            !budgeting.isOverflowAllowed
        ) {
            return DatabaseResultMessage.CurrentBudgetAmountExceedsMaximumLimit
        }

        financeRepository.createExpenseTransaction(transaction)
        return DatabaseResultMessage.CreateTransactionSuccess
    }
}
