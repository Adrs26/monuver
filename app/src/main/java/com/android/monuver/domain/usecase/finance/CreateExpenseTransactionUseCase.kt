package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AddTransactionState
import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.AccountRepository
import com.android.monuver.domain.repository.BudgetRepository
import com.android.monuver.domain.repository.FinanceRepository
import com.android.monuver.utils.BudgetWarningCondition
import com.android.monuver.utils.DateHelper

class CreateExpenseTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository,
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(transactionState: AddTransactionState): DatabaseResultState {
        when {
            transactionState.title.isEmpty() -> return DatabaseResultState.EmptyTransactionTitle
            transactionState.childCategory == 0 -> return DatabaseResultState.EmptyTransactionCategory
            transactionState.date.isEmpty() -> return DatabaseResultState.EmptyTransactionDate
            transactionState.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
            transactionState.sourceId == 0 -> return DatabaseResultState.EmptyTransactionSource
        }

        val (month, year) = DateHelper.getMonthAndYear(transactionState.date)
        val transaction = TransactionState(
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
            return DatabaseResultState.InsufficientAccountBalance
        }

        if (
            budget != null &&
            budget.usedAmount + transaction.amount > budget.maxAmount &&
            !budget.isOverflowAllowed
        ) {
            return DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit
        }

        financeRepository.createExpenseTransaction(transaction)

        if (budget != null) {
            val budgetUsage = budgetRepository.getBudgetUsagePercentage(budget.category)
            return calculateBudgetUsage(budgetUsage, budget.category)
        }

        return DatabaseResultState.CreateTransactionSuccess
    }

    private fun calculateBudgetUsage(budgetUsage: Float, budgetCategory: Int): DatabaseResultState {
        return when {
            budgetUsage > 100F -> {
                DatabaseResultState.CreateSuccessWithWarningCondition(
                    category = budgetCategory,
                    warningCondition = BudgetWarningCondition.OVER_BUDGET
                )
            }
            budgetUsage >= 100F -> DatabaseResultState.CreateSuccessWithWarningCondition(
                category = budgetCategory,
                warningCondition = BudgetWarningCondition.FULL_BUDGET
            )
            budgetUsage >= 80F -> DatabaseResultState.CreateSuccessWithWarningCondition(
                category = budgetCategory,
                warningCondition = BudgetWarningCondition.LOW_REMAINING_BUDGET
            )
            else -> DatabaseResultState.CreateTransactionSuccess
        }
    }
}
