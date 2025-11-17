package com.android.monuver.feature.transaction.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.repository.CoreRepository
import com.android.monuver.core.domain.util.BudgetWarningCondition
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.feature.transaction.domain.model.AddTransactionState
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateExpenseTransactionUseCase(
    private val coreRepository: CoreRepository,
    private val transactionRepository: TransactionRepository
) {
    @OptIn(ExperimentalTime::class)
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
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = transactionState.amount,
            sourceId = transactionState.sourceId,
            sourceName = transactionState.sourceName,
            isLocked = false,
            isSpecialCase = false
        )

        val accountBalance = coreRepository.getAccountBalance(transaction.sourceId)
        val budget = coreRepository.getBudgetForDate(
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

        transactionRepository.createExpenseTransaction(transaction)

        if (budget != null) {
            val budgetUsage = transactionRepository.getBudgetUsagePercentage(budget.category)
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