package com.android.monuver.feature.transaction.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.repository.CoreRepository
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.feature.transaction.domain.common.BudgetStatusState
import com.android.monuver.feature.transaction.domain.model.EditTransactionState
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class UpdateExpenseTransactionUseCase(
    private val coreRepository: CoreRepository,
    private val transactionRepository: TransactionRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(transactionState: EditTransactionState): DatabaseResultState {
        when {
            transactionState.title.isEmpty() -> return DatabaseResultState.EmptyTransactionTitle
            transactionState.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
        }

        val (month, year) = DateHelper.getMonthAndYear(transactionState.date)
        val transaction = TransactionState(
            id = transactionState.id,
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
            isLocked = transactionState.isLocked,
            isSpecialCase = false
        )

        val accountBalance = coreRepository.getAccountBalance(transaction.sourceId)
        val difference = transaction.amount - transactionState.initialAmount

        if (accountBalance == null || accountBalance < difference) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        val oldBudget = coreRepository.getBudgetForDate(
            category = transactionState.initialParentCategory,
            date = transactionState.initialDate
        )
        val newBudget = coreRepository.getBudgetForDate(
            category = transaction.parentCategory,
            date = transaction.date
        )

        val budgetStatus = getBudgetStatus(oldBudget, newBudget)

        newBudget?.let { budget ->
            if (!budget.isOverflowAllowed) {
                val amountToAdd = when (budgetStatus) {
                    BudgetStatusState.SameBudget -> difference
                    BudgetStatusState.DifferentBudget, BudgetStatusState.NoOldBudget -> transaction.amount
                    else -> 0L
                }

                if (amountToAdd > 0 && budget.usedAmount + amountToAdd > budget.maxAmount) {
                    return DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit
                }
            }
        }

        transactionRepository.updateExpenseTransaction(
            transactionState = transaction,
            initialParentCategory = transactionState.initialParentCategory,
            initialDate = transactionState.initialDate,
            initialAmount = transactionState.initialAmount,
            budgetStatus = budgetStatus
        )
        return DatabaseResultState.UpdateTransactionSuccess
    }

    private fun getBudgetStatus(oldBudgetState: BudgetState?, newBudgetState: BudgetState?): BudgetStatusState {
        return when {
            oldBudgetState == null && newBudgetState != null -> BudgetStatusState.NoOldBudget
            oldBudgetState != null && newBudgetState == null -> BudgetStatusState.NoNewBudget
            oldBudgetState == null && newBudgetState == null -> BudgetStatusState.NoBudget
            oldBudgetState?.category == newBudgetState?.category -> BudgetStatusState.SameBudget
            else -> BudgetStatusState.DifferentBudget
        }
    }
}