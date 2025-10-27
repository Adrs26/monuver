package com.android.monu.domain.usecase.finance

import com.android.monu.domain.common.BudgetStatusState
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.BudgetState
import com.android.monu.domain.model.EditTransactionState
import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.utils.DateHelper

class UpdateExpenseTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository,
    private val budgetRepository: BudgetRepository
) {
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
            timeStamp = System.currentTimeMillis(),
            amount = transactionState.amount,
            sourceId = transactionState.sourceId,
            sourceName = transactionState.sourceName,
            isLocked = transactionState.isLocked,
            isSpecialCase = false
        )

        val accountBalance = accountRepository.getAccountBalance(transaction.sourceId)
        val difference = transaction.amount - transactionState.initialAmount

        if (accountBalance == null || accountBalance < difference) {
            return DatabaseResultState.InsufficientAccountBalance
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
                    BudgetStatusState.SameBudget -> difference
                    BudgetStatusState.DifferentBudget, BudgetStatusState.NoOldBudget -> transaction.amount
                    else -> 0L
                }

                if (amountToAdd > 0 && budget.usedAmount + amountToAdd > budget.maxAmount) {
                    return DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit
                }
            }
        }

        financeRepository.updateExpenseTransaction(
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
