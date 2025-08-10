package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BudgetingRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.screen.transaction.edittransaction.components.EditTransactionContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.presentation.utils.DateHelper

class UpdateExpenseTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository,
    private val budgetingRepository: BudgetingRepository
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
            sourceName = transactionState.sourceName
        )

        val accountBalance = accountRepository.getAccountBalance(transaction.sourceId)
        val difference = transaction.amount - transactionState.initialAmount

        if (accountBalance == null || accountBalance < difference) {
            return DatabaseResultMessage.InsufficientAccountBalance
        }

        val oldBudgeting = budgetingRepository.getBudgetingForDate(
            category = transactionState.initialParentCategory,
            date = transactionState.initialDate
        )
        val newBudgeting = budgetingRepository.getBudgetingForDate(
            category = transaction.parentCategory,
            date = transaction.date
        )

        val budgetingStatus = getBudgetingStatus(oldBudgeting, newBudgeting)

        newBudgeting?.let { budget ->
            if (!budget.isOverflowAllowed) {
                val amountToAdd = when (budgetingStatus) {
                    BudgetingStatus.SameBudgeting -> difference
                    BudgetingStatus.DifferentBudgeting, BudgetingStatus.NoOldBudgeting -> transaction.amount
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
            budgetingStatus = budgetingStatus
        )
        return DatabaseResultMessage.UpdateTransactionSuccess
    }

    private fun getBudgetingStatus(oldBudgeting: Budgeting?, newBudgeting: Budgeting?): BudgetingStatus {
        return when {
            oldBudgeting == null && newBudgeting != null -> BudgetingStatus.NoOldBudgeting
            oldBudgeting != null && newBudgeting == null -> BudgetingStatus.NoNewBudgeting
            oldBudgeting == null && newBudgeting == null -> BudgetingStatus.NoBudgeting
            oldBudgeting?.category == newBudgeting?.category -> BudgetingStatus.SameBudgeting
            else -> BudgetingStatus.DifferentBudgeting
        }
    }
}

sealed class BudgetingStatus {
    object NoOldBudgeting : BudgetingStatus()
    object NoNewBudgeting : BudgetingStatus()
    object NoBudgeting : BudgetingStatus()
    object SameBudgeting : BudgetingStatus()
    object DifferentBudgeting : BudgetingStatus()
}
