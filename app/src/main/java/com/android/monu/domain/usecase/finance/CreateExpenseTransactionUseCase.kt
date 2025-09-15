package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.screen.transaction.addTransaction.components.AddTransactionContentState
import com.android.monu.ui.feature.utils.BudgetWarningCondition
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.DateHelper

class CreateExpenseTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository,
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(transactionState: AddTransactionContentState): DatabaseResultMessage {
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
            isLocked = false,
            isSpecialCase = false
        )

        val accountBalance = accountRepository.getAccountBalance(transaction.sourceId)
        val budget = budgetRepository.getBudgetForDate(
            category = transaction.parentCategory,
            date = transaction.date
        )

        if (accountBalance == null || accountBalance < transaction.amount) {
            return DatabaseResultMessage.InsufficientAccountBalance
        }

        if (
            budget != null &&
            budget.usedAmount + transaction.amount > budget.maxAmount &&
            !budget.isOverflowAllowed
        ) {
            return DatabaseResultMessage.CurrentBudgetAmountExceedsMaximumLimit
        }

        financeRepository.createExpenseTransaction(transaction)

        if (budget != null) {
            val budgetUsage = budgetRepository.getBudgetUsagePercentage(budget.category)
            return calculateBudgetUsage(budgetUsage, budget.category)
        }

        return DatabaseResultMessage.CreateTransactionSuccess
    }

    private fun calculateBudgetUsage(budgetUsage: Float, budgetCategory: Int): DatabaseResultMessage {
        return when {
            budgetUsage > 100F -> {
                DatabaseResultMessage.CreateSuccessWithWarningCondition(
                    category = budgetCategory,
                    warningCondition = BudgetWarningCondition.OVER_BUDGET
                )
            }
            budgetUsage >= 100F -> DatabaseResultMessage.CreateSuccessWithWarningCondition(
                category = budgetCategory,
                warningCondition = BudgetWarningCondition.FULL_BUDGET
            )
            budgetUsage >= 80F -> DatabaseResultMessage.CreateSuccessWithWarningCondition(
                category = budgetCategory,
                warningCondition = BudgetWarningCondition.LOW_REMAINING_BUDGET
            )
            else -> DatabaseResultMessage.CreateTransactionSuccess
        }
    }
}
