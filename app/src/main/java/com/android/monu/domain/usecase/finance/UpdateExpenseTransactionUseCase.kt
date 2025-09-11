package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.screen.transaction.edittransaction.components.EditTransactionContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.DateHelper

class UpdateExpenseTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository,
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(transactionState: EditTransactionContentState): DatabaseResultMessage {
        when {
            transactionState.title.isEmpty() -> return DatabaseResultMessage.EmptyTransactionTitle
            transactionState.amount == 0L -> return DatabaseResultMessage.EmptyTransactionAmount
        }

        val (month, year) = DateHelper.getMonthAndYear(transactionState.date)
        val transaction = Transaction(
            id = transactionState.id,
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
            isLocked = transactionState.isLocked
        )

        val accountBalance = accountRepository.getAccountBalance(transaction.sourceId)
        val difference = transaction.amount - transactionState.initialAmount

        if (accountBalance == null || accountBalance < difference) {
            return DatabaseResultMessage.InsufficientAccountBalance
        }

        val oldBudget = budgetRepository.getBudgetForDate(
            category = transactionState.initialParentCategory,
            date = transactionState.initialDate
        )
        val newBudget = budgetRepository.getBudgetForDate(
            category = transaction.parentCategory,
            date = transaction.date
        )

        val budgetStatus = getBudgetStatus(oldBudget, newBudget)

        newBudget?.let { budget ->
            if (!budget.isOverflowAllowed) {
                val amountToAdd = when (budgetStatus) {
                    BudgetStatus.SameBudget -> difference
                    BudgetStatus.DifferentBudget, BudgetStatus.NoOldBudget -> transaction.amount
                    else -> 0L
                }

                if (amountToAdd > 0 && budget.usedAmount + amountToAdd > budget.maxAmount) {
                    return DatabaseResultMessage.CurrentBudgetAmountExceedsMaximumLimit
                }
            }
        }

        financeRepository.updateExpenseTransaction(
            transaction = transaction,
            initialParentCategory = transactionState.initialParentCategory,
            initialDate = transactionState.initialDate,
            initialAmount = transactionState.initialAmount,
            budgetStatus = budgetStatus
        )
        return DatabaseResultMessage.UpdateTransactionSuccess
    }

    private fun getBudgetStatus(oldBudget: Budget?, newBudget: Budget?): BudgetStatus {
        return when {
            oldBudget == null && newBudget != null -> BudgetStatus.NoOldBudget
            oldBudget != null && newBudget == null -> BudgetStatus.NoNewBudget
            oldBudget == null && newBudget == null -> BudgetStatus.NoBudget
            oldBudget?.category == newBudget?.category -> BudgetStatus.SameBudget
            else -> BudgetStatus.DifferentBudget
        }
    }
}

sealed class BudgetStatus {
    object NoOldBudget : BudgetStatus()
    object NoNewBudget : BudgetStatus()
    object NoBudget : BudgetStatus()
    object SameBudget : BudgetStatus()
    object DifferentBudget : BudgetStatus()
}
